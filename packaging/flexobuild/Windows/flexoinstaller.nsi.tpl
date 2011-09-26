# Auto-generated by EclipseNSIS Script Wizard
# 30-juin-2011 11:40:39

Name @product.name@

SetCompressor lzma

# General Symbol Definitions
!define REGKEY "SOFTWARE\$(^Name)"
!define VERSION @flexo_version@
!define COMPANY @organization.name@
!define URL @organization.url@

# MUI Symbol Definitions
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install-blue.ico"
!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_LICENSEPAGE_RADIOBUTTONS
!define MUI_STARTMENUPAGE_REGISTRY_ROOT HKLM
!define MUI_STARTMENUPAGE_REGISTRY_KEY ${REGKEY}
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME StartMenuGroup
!define MUI_STARTMENUPAGE_DEFAULTFOLDER @product.name@
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall-blue.ico"
!define MUI_UNFINISHPAGE_NOAUTOCLOSE
!define MUI_LANGDLL_REGISTRY_ROOT HKLM
!define MUI_LANGDLL_REGISTRY_KEY ${REGKEY}
!define MUI_LANGDLL_REGISTRY_VALUENAME InstallerLanguage

!define BUSINESS_VERSION "@productSuffix@"
!define PRODUCT_NAME "$(^Name) ${BUSINESS_VERSION} ${VERSION}"
!define EXE_FILE "${PRODUCT_NAME}.exe"
Icon "@dist.dir@\Icons\Flexo\@wizard.setup.icon@"

# Included files
!include Sections.nsh
!include MUI2.nsh

# Reserved Files
!insertmacro MUI_RESERVEFILE_LANGDLL
ReserveFile "${NSISDIR}\Plugins\System.dll"

# Variables
Var StartMenuGroup

!define MUI_FINISHPAGE_RUN "$INSTDIR\${EXE_FILE}"
!define MUI_FINISHPAGE_RUN_TEXT "Launch ${PRODUCTNAME}"

# Installer pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE  @dist.dir@\License\@versionType@\License.rtf
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_STARTMENU Application $StartMenuGroup
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

# Installer languages
!insertmacro MUI_LANGUAGE English
!insertmacro MUI_LANGUAGE French
!insertmacro MUI_LANGUAGE Dutch

# Installer attributes
OutFile "@project.build.directory@\@product.name@ @productSuffix@ @flexo_version@ Setup.exe"
InstallDir "$PROGRAMFILES\@product.name@\@productSuffix@\${VERSION}"
CRCCheck on
XPStyle on
ShowInstDetails show
VIProductVersion @flexo_version@.0
VIAddVersionKey /LANG=${LANG_ENGLISH} ProductName @product.name@
VIAddVersionKey /LANG=${LANG_ENGLISH} ProductVersion "${VERSION}"
VIAddVersionKey /LANG=${LANG_ENGLISH} CompanyName "${COMPANY}"
VIAddVersionKey /LANG=${LANG_ENGLISH} CompanyWebsite "${URL}"
VIAddVersionKey /LANG=${LANG_ENGLISH} FileVersion "${VERSION}"
VIAddVersionKey /LANG=${LANG_ENGLISH} FileDescription ""
VIAddVersionKey /LANG=${LANG_ENGLISH} LegalCopyright "@organization.name@"
InstallDirRegKey HKLM "${REGKEY}" Path
ShowUninstDetails show

# Installer sections
# Macro for creating a registry key
!define HKEY_CLASSES_ROOT 0x80000000
!define HKEY_CURRENT_USER 0x80000001
!define HKEY_LOCAL_MACHINE 0x80000002
!define HKEY_USERS 0x80000003
!define HKEY_PERFORMANCE_DATA 0x80000004
!define HKEY_CURRENT_CONFIG 0x80000005
!define HKEY_DYN_DATA 0x80000006
!define KEY_CREATE_SUB_KEY 0x0004
!macro CreateRegKey ROOT_KEY SUB_KEY
    Push $0
    Push $1
    System::Call /NOUNLOAD "Advapi32::RegCreateKeyExA(i, t, i, t, i, i, i, *i, i) i(${ROOT_KEY}, '${SUB_KEY}', 0, '', 0, ${KEY_CREATE_SUB_KEY}, 0, .r0, 0) .r1"
    StrCmp $1 0 +2
    SetErrors
    StrCmp $0 0 +2
    System::Call /NOUNLOAD "Advapi32::RegCloseKey(i) i(r0) .r1"
    System::Free 0
    Pop $1
    Pop $0
!macroend

Section -Main SEC0000
    SetOutPath $INSTDIR
    SetOverwrite on
    File /r "@dist.dir@\*"
    !insertmacro CreateRegKey ${HKEY_CURRENT_USER} Software\JavaSoft\Prefs\org\openflexo\application\${VERSION}
    WriteRegStr HKEY_CURRENT_USER Software\JavaSoft\Prefs\org\openflexo\application\${VERSION} language {$LANGUAGE}
    WriteRegStr HKLM "${REGKEY}\Components" Main 1
SectionEnd

Section -post SEC0001
    WriteRegStr HKLM "${REGKEY}" Path $INSTDIR
    SetOutPath $INSTDIR
    WriteUninstaller $INSTDIR\uninstall.exe
    !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
    CreateDirectory "$SMPROGRAMS\$StartMenuGroup"
    SetOutPath $SMPROGRAMS\$StartMenuGroup
    CreateShortCut "$SMPROGRAMS\$StartMenuGroup\${PRODUCT_NAME}.lnk" "$INSTDIR\${EXE_FILE}"
    CreateShortcut "$SMPROGRAMS\$StartMenuGroup\$(^UninstallLink).lnk" $INSTDIR\uninstall.exe
    !insertmacro MUI_STARTMENU_WRITE_END
    CreateShortCut "$DESKTOP\${PRODUCT_NAME}.lnk" "$INSTDIR\${EXE_FILE}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayName "$(^Name)"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayVersion "${VERSION}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" Publisher "${COMPANY}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" URLInfoAbout "${URL}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayIcon $INSTDIR\uninstall.exe
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" UninstallString $INSTDIR\uninstall.exe
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoModify 1
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoRepair 1
    
SectionEnd

# Macro for selecting uninstaller sections
!macro SELECT_UNSECTION SECTION_NAME UNSECTION_ID
    Push $R0
    ReadRegStr $R0 HKLM "${REGKEY}\Components" "${SECTION_NAME}"
    StrCmp $R0 1 0 next${UNSECTION_ID}
    !insertmacro SelectSection "${UNSECTION_ID}"
    GoTo done${UNSECTION_ID}
next${UNSECTION_ID}:
    !insertmacro UnselectSection "${UNSECTION_ID}"
done${UNSECTION_ID}:
    Pop $R0
!macroend

# Uninstaller sections
Section /o -un.Main UNSEC0000
    DeleteRegValue HKEY_CURRENT_USER Software\JavaSoft\Prefs\org\openflexo\application\${VERSION} language
    DeleteRegKey /IfEmpty HKEY_CURRENT_USER Software\JavaSoft\Prefs\org\openflexo\application\${VERSION}
    DeleteRegKey /IfEmpty HKEY_CURRENT_USER Software\JavaSoft\Prefs\org\openflexo\application
    DeleteRegKey /IfEmpty HKEY_CURRENT_USER Software\JavaSoft\Prefs\org\openflexo
    DeleteRegKey /IfEmpty HKEY_CURRENT_USER Software\JavaSoft\Prefs\org
    RmDir /r /REBOOTOK $INSTDIR
    DeleteRegValue HKLM "${REGKEY}\Components" Main
SectionEnd

Section -un.post UNSEC0001
    DeleteRegKey HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\${PRODUCT_NAME}.lnk" "$INSTDIR\${EXE_FILE}"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\$(^UninstallLink).lnk" $INSTDIR\uninstall.exe
    Delete /REBOOTOK "$DESKTOP\${PRODUCT_NAME}.lnk" "$INSTDIR\${EXE_FILE}"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\$(^UninstallLink).lnk"
    Delete /REBOOTOK $INSTDIR\uninstall.exe
    DeleteRegValue HKLM "${REGKEY}" StartMenuGroup
    DeleteRegValue HKLM "${REGKEY}" Path
    DeleteRegKey /IfEmpty HKLM "${REGKEY}\Components"
    DeleteRegKey /IfEmpty HKLM "${REGKEY}"
    RmDir /REBOOTOK $SMPROGRAMS\$StartMenuGroup
    RmDir /REBOOTOK $INSTDIR
    Push $R0
    StrCpy $R0 $StartMenuGroup 1
    StrCmp $R0 ">" no_smgroup
no_smgroup:
    Pop $R0
SectionEnd

# Installer functions
Function .onInit
    InitPluginsDir
    !insertmacro MUI_LANGDLL_DISPLAY
FunctionEnd

# Uninstaller functions
Function un.onInit
    ReadRegStr $INSTDIR HKLM "${REGKEY}" Path
    !insertmacro MUI_STARTMENU_GETFOLDER Application $StartMenuGroup
    !insertmacro MUI_UNGETLANGUAGE
    !insertmacro SELECT_UNSECTION Main ${UNSEC0000}
FunctionEnd

# Installer Language Strings
# TODO Update the Language Strings with the appropriate translations.s

LangString ^UninstallLink ${LANG_ENGLISH} "Uninstall ${PRODUCT_NAME}"
LangString ^UninstallLink ${LANG_FRENCH} "Uninstall ${PRODUCT_NAME}"
LangString ^UninstallLink ${LANG_DUTCH} "Uninstall ${PRODUCT_NAME}"
