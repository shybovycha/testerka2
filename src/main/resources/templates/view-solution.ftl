<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Testerka v2. Solution ${solution.id}</title>

    <link rel="stylesheet" href="/bower_components/bootstrap/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="/bower_components/bootstrap/dist/css/bootstrap-theme.min.css">
</head>
<body>
<div class="container-fluid">
    <h1>Status: ${solution.statusString?html}</h1>

    <#if solution.errorMessage?exists && solution.errorMessage?length != 0>
        <div class="panel panel-danger">
            <div class="panel-heading">Odpowiedź systemu</div>
            <div class="panel-body">
                ${solution.errorMessage}
            </div>
        </div>
    </#if>

    <#if solution.results?has_content>
    <table class="table table-hover">
        <thead>
            <tr>
                <th>Wejście</th>
                <th>Wyjście</th>
                <th>Poprawność</th>
            </tr>
        </thead>

        <tbody>
            <#list solution.results as result>
            <tr>
                <td>
                    <pre>${result.testCase.input!?html}</pre>
                </td>

                <td>
                    <pre><#if result.output?length == 0>${"<EMPTY>"?html}<#else>${result.output?html}</#if></pre>
                </td>

                <td>
                    <#if result.passed>
                        <i class="glyphicon glyphicon-ok"></i>
                    <#else>
                        <i class="glyphicon glyphicon-remove"></i>
                    </#if>
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
    </#if>
</div>
</body>
</html>