'Option Explicit
Dim oShell : Set oShell  = CreateObject("Shell.Application")
Dim oFSO : Set oFSO = CreateObject("Scripting.FileSystemObject")
Dim oFolder : Set oFolder = oShell.Namespace(oFSO.GetParentFolderName(Wscript.ScriptFullName))
Dim oFile, iPos, sHeader(999), sVal, i, msg
Set fso = createobject("scripting.filesystemobject") 

' Get a list of the property names
For iPos = 0 to 999
    sHeader(iPos) = Replace(oFolder.GetDetailsOf(oFolder.Items, iPos)," ","_")
Next

WScript.Echo "Starting get file attributes"
' For each file in this folder, print the property name and the value
set propFile = fso.CreateTextFile("file.properties",true, true)

i = 0
For Each oFile in oFolder.Items
    i = i + 1
    msg = ""	
    'WScript.Echo "Looking at " & oFile.Name
    For iPos = 0 To 999
        sVal = oFolder.GetDetailsOf(oFolder.ParseName(oFile.Name), iPos)
        If sVal <> "" Then 
			  msg = cstr(i & "." & sHeader(iPos) & "=" & sVal)
'			  WScript.Echo msg & TypeName(msg)
    		  propFile.WriteLine(msg)
     	end if
    Next	
Next
propFile.Close
WScript.Echo "finished get file attributes. file count = " & i
