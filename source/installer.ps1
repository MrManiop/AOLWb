Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

$BaseDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$AppsDir = Join-Path $BaseDir "apps"
$LogPath = "C:\LOGS\installed_apps.log"

New-Item -Path $LogPath -ItemType File -Force | Out-Null

$folders = Get-ChildItem -Path $AppsDir -Directory | Sort-Object Name

foreach ($folder in $folders) {
    $appName = $folder.Name
    $cmdPath = Join-Path $folder.FullName "install.cmd"

    if (-not (Test-Path -Path $cmdPath -PathType Leaf)) {
        continue
    }

    Start-Process `
        -FilePath "cmd.exe" `
        -ArgumentList "/c `"$cmdPath`"" `
        -WorkingDirectory $folder.FullName `
        -Verb RunAs `
        -Wait

    Add-Content -Path $LogPath -Value $appName -Encoding UTF8
}

