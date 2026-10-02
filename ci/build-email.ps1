<#
  Builds the Jenkins email report from allure-results.

  Modes:
    -SummaryOnly  : writes email\summary.properties (TOTAL, PASSED, FAILED, SKIPPED, SUITE)
    (default)     : also writes email\email-body.html and the Allure report attachment in email\attach\

  Inputs (environment variables, set by the Jenkinsfile):
    JOB_NAME, BUILD_NUMBER, BUILD_RESULT, RUN_MODE, TRIGGER_TEXT,
    BUILD_START_MS, BUILD_DURATION_MS, ALLURE_CLI (optional, Allure install folder)

  Compatible with Windows PowerShell 5.1.
#>
param([switch]$SummaryOnly)

$ErrorActionPreference = 'Stop'
$root       = (Get-Location).Path
$resultsDir = Join-Path $root 'allure-results'
$outDir     = Join-Path $root 'email'
$attachDir  = Join-Path $outDir 'attach'
$template   = Join-Path $root 'ci\email-template.html'
New-Item -ItemType Directory -Force -Path $attachDir | Out-Null

function Enc([string]$s) { return [System.Net.WebUtility]::HtmlEncode($s) }

function Write-Utf8([string]$path, [string]$text) {
    [System.IO.File]::WriteAllText($path, $text, (New-Object System.Text.UTF8Encoding $false))
}

function Format-Seconds([double]$sec) {
    if ($sec -lt 60) { return ('{0:0.0}s' -f $sec) }
    $m = [math]::Floor($sec / 60); $s = [math]::Floor($sec % 60)
    return ('{0}m {1}s' -f $m, $s)
}

# Readable names for test methods (unknown names are shown as they are)
$readable = @{
    'CreateUser'             = 'Register a new account'
    'editAccountDetails'     = 'Edit account details'
    'logOutTestapp'          = 'Log out'
    'loginTestApp'           = 'Log in'
    'searchandaddtowishlist' = 'Search and add to wishlist'
    'AddtoWishList'          = 'Add to cart from wishlist'
    'RemoveFromWishList'     = 'Remove from wishlist'
}

# ---------- Read results ----------
$tests  = @{}   # key "test|browser" -> latest result
$suite  = ''
$files  = @()
if (Test-Path $resultsDir) { $files = Get-ChildItem -Path $resultsDir -Filter '*-result.json' }

foreach ($f in $files) {
    $r = Get-Content -Path $f.FullName -Raw -Encoding UTF8 | ConvertFrom-Json

    $browser = ''
    foreach ($p in @($r.parameters)) { if ($p -and $p.name -eq 'browser') { $browser = [string]$p.value } }
    foreach ($l in @($r.labels)) {
        if ($l -and $l.name -eq 'parentSuite' -and -not $suite) { $suite = [string]$l.value }
        if ($l -and $l.name -eq 'suite' -and -not $browser) { $browser = ([string]$l.value) -replace 'Test$', '' }
    }
    if (-not $browser) { $browser = 'Unknown' }
    $browser = $browser.Substring(0, 1).ToUpper() + $browser.Substring(1).ToLower()

    $status = switch ([string]$r.status) { 'passed' { 'PASS' } 'skipped' { 'SKIP' } default { 'FAIL' } }
    $start  = [double]0; $stop = [double]0
    if ($r.start) { $start = [double]$r.start }
    if ($r.stop)  { $stop  = [double]$r.stop }

    $key = "$($r.name)|$browser"
    # If a test ran more than once (retry), keep the latest attempt
    if (-not $tests.ContainsKey($key) -or $tests[$key].Stop -lt $stop) {
        $tests[$key] = [pscustomobject]@{
            Name = [string]$r.name; Browser = $browser; Status = $status
            Start = $start; Stop = $stop
            Seconds = $(if ($status -ne 'SKIP' -and $stop -gt $start) { ($stop - $start) / 1000 } else { $null })
        }
    }
}

$all     = @($tests.Values)
$total   = $all.Count
$passed  = @($all | Where-Object { $_.Status -eq 'PASS' }).Count
$failed  = @($all | Where-Object { $_.Status -eq 'FAIL' }).Count
$skipped = @($all | Where-Object { $_.Status -eq 'SKIP' }).Count
if (-not $suite) { $suite = '-' }

Write-Utf8 (Join-Path $outDir 'summary.properties') ("TOTAL=$total`r`nPASSED=$passed`r`nFAILED=$failed`r`nSKIPPED=$skipped`r`nSUITE=$suite`r`n")
Write-Host "Results: total=$total passed=$passed failed=$failed skipped=$skipped suite=$suite"
if ($SummaryOnly) { return }

# ---------- Browsers (known order first) and tests (order they ran) ----------
$known    = @('Chrome', 'Firefox', 'Edge')
$present  = @($all | ForEach-Object { $_.Browser } | Sort-Object -Unique)
$browsers = @($known | Where-Object { $present -contains $_ }) + @($present | Where-Object { $known -notcontains $_ })

$testOrder = @($all | Group-Object Name | ForEach-Object {
    $first = ($_.Group | Where-Object { $_.Start -gt 0 } | Measure-Object -Property Start -Minimum).Minimum
    if (-not $first) { $first = [double]::MaxValue }
    [pscustomobject]@{ Name = $_.Name; First = $first }
} | Sort-Object First | ForEach-Object { $_.Name })

# ---------- Results table ----------
$pill = @{
    'PASS' = @('#047857', '#ECFDF5')
    'FAIL' = @('#B91C1C', '#FEF2F2')
    'SKIP' = @('#4B5563', '#F3F4F6')
}
$cellBorder = 'border-bottom:1px solid #E5E7EB;'
$sb = New-Object System.Text.StringBuilder

[void]$sb.Append("<tr style=`"background:#F9FAFB;`"><td style=`"padding:9px 12px;font-weight:600;color:#6B7280;$cellBorder`">Test case</td>")
foreach ($b in $browsers) { [void]$sb.Append("<td align=`"center`" style=`"padding:9px 8px;font-weight:600;color:#6B7280;$cellBorder`">$(Enc $b)</td>") }
[void]$sb.Append("</tr>`r`n")

if ($total -eq 0) {
    [void]$sb.Append("<tr><td style=`"padding:14px 12px;color:#6B7280;`">No tests ran in this build.</td></tr>`r`n")
} else {
    $totals = @{}; foreach ($b in $browsers) { $totals[$b] = [double]0 }
    foreach ($t in $testOrder) {
        $name = $t; if ($readable.ContainsKey($t)) { $name = $readable[$t] }
        [void]$sb.Append("<tr><td style=`"padding:9px 12px;$cellBorder`">$(Enc $name)</td>")
        foreach ($b in $browsers) {
            $key = "$t|$b"
            if ($tests.ContainsKey($key)) {
                $x = $tests[$key]; $c = $pill[$x.Status]
                $time = '&ndash;'
                if ($x.Seconds -ne $null) { $time = Format-Seconds $x.Seconds; $totals[$b] += $x.Seconds }
                [void]$sb.Append("<td align=`"center`" style=`"padding:7px 8px;white-space:nowrap;$cellBorder`"><span style=`"display:inline-block;background:$($c[1]);color:$($c[0]);font-weight:700;font-size:11px;border-radius:3px;padding:2px 6px;`">$($x.Status)</span> <span style=`"color:#6B7280;`">$time</span></td>")
            } else {
                [void]$sb.Append("<td align=`"center`" style=`"padding:7px 8px;color:#9CA3AF;$cellBorder`">&ndash;</td>")
            }
        }
        [void]$sb.Append("</tr>`r`n")
    }
    [void]$sb.Append("<tr style=`"background:#F9FAFB;`"><td style=`"padding:9px 12px;font-weight:600;`">Total time</td>")
    foreach ($b in $browsers) { [void]$sb.Append("<td align=`"center`" style=`"padding:9px 8px;font-weight:600;`">$(Format-Seconds $totals[$b])</td>") }
    [void]$sb.Append("</tr>`r`n")
}

# ---------- Header values ----------
$result = [string]$env:BUILD_RESULT
$statusMap = @{
    'SUCCESS'  = @('PASSED',   '#047857', '#ECFDF5')
    'UNSTABLE' = @('UNSTABLE', '#B45309', '#FFFBEB')
    'FAILURE'  = @('FAILED',   '#B91C1C', '#FEF2F2')
    'ABORTED'  = @('ABORTED',  '#4B5563', '#F3F4F6')
}
if (-not $statusMap.ContainsKey($result)) { $result = 'FAILURE' }
$st = $statusMap[$result]

$mode = 'Local'; if ($env:RUN_MODE -eq 'grid') { $mode = 'Grid (parallel)' }

$started = '-'
if ($env:BUILD_START_MS) {
    $started = ([DateTimeOffset]::FromUnixTimeMilliseconds([int64]$env:BUILD_START_MS)).ToLocalTime().ToString('dd MMM yyyy, hh:mm tt')
}
$duration = '-'
if ($env:BUILD_DURATION_MS) { $duration = Format-Seconds ([double]$env:BUILD_DURATION_MS / 1000) }

$browserText = '-'; if ($browsers.Count -gt 0) { $browserText = $browsers -join ', ' }

function Or-Dash([string]$v) { if ([string]::IsNullOrWhiteSpace($v)) { return '-' } return $v }

$values = @{
    'JOB' = (Or-Dash $env:JOB_NAME); 'BUILD' = (Or-Dash $env:BUILD_NUMBER)
    'STATUS_LABEL' = $st[0]; 'STATUS_FG' = $st[1]; 'STATUS_BG' = $st[2]
    'SUITE' = $suite; 'BROWSERS' = $browserText; 'MODE' = $mode
    'TRIGGER' = (Or-Dash $env:TRIGGER_TEXT); 'STARTED' = $started; 'DURATION' = $duration
    'TOTAL' = "$total"; 'PASSED' = "$passed"; 'FAILED' = "$failed"; 'SKIPPED' = "$skipped"
}
$html = Get-Content -Path $template -Raw -Encoding UTF8
foreach ($k in $values.Keys) { $html = $html.Replace("{{$k}}", (Enc ([string]$values[$k]))) }
$html = $html.Replace('{{TABLE_ROWS}}', $sb.ToString())
Write-Utf8 (Join-Path $outDir 'email-body.html') $html
Write-Host 'Email body written to email\email-body.html'

# ---------- Allure report attachment ----------
if ($total -eq 0) { Write-Host 'No results, so no report is attached.'; return }

$attachName = "Allure-Report-Build-$($env:BUILD_NUMBER)"
$singleOk = $false
if ($env:ALLURE_CLI) {
    $cli = Join-Path $env:ALLURE_CLI 'bin\allure.bat'
    if (Test-Path $cli) {
        $tmpOut = Join-Path $outDir 'single'
        & $cli generate $resultsDir --single-file --clean -o $tmpOut 2>&1 | Write-Host
        $index = Join-Path $tmpOut 'index.html'
        if ($LASTEXITCODE -eq 0 -and (Test-Path $index)) {
            Copy-Item $index (Join-Path $attachDir "$attachName.html") -Force
            $singleOk = $true
            Write-Host "Attached single-file report: $attachName.html"
        }
    }
}
if (-not $singleOk) {
    # Fallback: zip the normal report folder created by the Allure plugin
    $reportDir = Join-Path $root 'allure-report'
    if (Test-Path $reportDir) {
        Compress-Archive -Path (Join-Path $reportDir '*') -DestinationPath (Join-Path $attachDir "$attachName.zip") -Force
        Write-Host "Single-file report not available, attached zip instead: $attachName.zip"
    } else {
        Write-Host 'No Allure report found to attach.'
    }
}
