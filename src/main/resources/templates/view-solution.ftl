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

    <#if solution.errorMessage?exists>
        <div class="panel panel-error">
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
                    <pre>${result.testCase.output!}</pre>
                </td>

                <td>
                    ${result.passedString}
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
    </#if>
</div>
</body>
</html>