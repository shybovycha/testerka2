<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Testerka v2. System error</title>

    <link rel="stylesheet" href="/bower_components/bootstrap/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="/bower_components/bootstrap/dist/css/bootstrap-theme.min.css">
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-xs-12">&nbsp;</div>
        <div class="col-xs-12">
            <div class="panel panel-danger">
                <div class="panel-heading">Wystąpił błąd systemu</div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-xs-4"><b>Status:</b></div>
                        <div class="col-xs-8">${status}</div>
                    </div>
                    <div class="row">
                        <div class="col-xs-4"><b>Komunikat:</b></div>
                        <div class="col-xs-8">${error}</div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12">Uprzejmie proszę o kontakt w tej sprawie z autorem systemu</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>