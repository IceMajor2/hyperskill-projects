<!DOCTYPE html>
<html>
<head>
    <title>Latest</title>
</head>
<body>
    <#list codes as code>
        <span id="load_date">${code.date}</span>
        <pre id="code_snippet">${code.code}</pre>
    </#list>
</body>
</html>