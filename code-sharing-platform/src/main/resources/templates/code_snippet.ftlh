<!DOCTYPE html>
<html>
<head>
    <title>Code</title>
    <link rel="stylesheet"
           href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
</head>
<body>
    <span id="load_date">${code.getDateFormatted()}</span>
    <br>
    <br>
    <#if code.isToBeViewRestricted()>
        <#if code.getViews() != 0>
            <u><span id="views_restriction">${code.getViews()}</span></u> more views allowed
        <#else>
            No more views allowed
        </#if>
    </#if>
    <#if code.isToBeTimeRestricted()>
        The code will be available for <u><span id="time_restriction">${code.getTime()}</span></u> seconds
    </#if>
    <pre id="code_snippet"><code>${code}</code></pre>
</body>
</html>