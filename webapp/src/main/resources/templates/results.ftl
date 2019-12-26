<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Testerka v2</title>

    <link rel="stylesheet" href="/node_modules/bootstrap/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="/node_modules/bootstrap/dist/css/bootstrap-theme.min.css">
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-xs-12">&nbsp;</div>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <h2>Matematyczne podstawy informatyki</h2>
        </div>
    </div>
    <div class="row">
        <div class="col-xs-12">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>Autor</th>
                    <th>Język</th>
                    <th>Odesłano</th>
                    <th>Punkty (%)</th>
                </tr>
                </thead>

                <tbody>
                <#list allSolutions as solution>
                <tr>
                    <td>
                    ${solution.author}
                    </td>

                    <td>
                    ${solution.language}
                    </td>

                    <td>
                        <a href="/solution/${solution.id}">${solution.createdAtStr}</a>
                    </td>

                    <td>
                    ${solution.points}
                    </td>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>