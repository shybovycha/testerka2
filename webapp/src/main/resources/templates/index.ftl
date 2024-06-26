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
        <div class="col-xs-6">
            <div class="panel panel-primary">
                <div class="panel-heading">Problem</div>

                <div class="panel-body">
                    Plansza o wymiarach <code>6x6</code> zawiera <code>N</code> samochodów.
                    Samochody na planszy są ustawione w pionie albo w poziomie,
                    oraz zajmują dwa lub trzy pola na planszy. Adres pola planszy składa się z punktu <code>(x,
                    y)</code>, gdzie
                    <code>x</code> i <code>y</code> są z
                    przedziału <code>[0, 5]</code> oraz lewy dolny róg planszy ma współrzędne <code>(0,
                    0)</code>
                    natomiast prawy
                    górny róg współrzędne
                    <code>(5, 5)</code>. Samochody mogą się poruszać tylko do przodu lub w tyłu zgodnie ze swoim
                    położeniem. Samochody nie
                    mogą wyjeżdzać poza granice planszy lub najeżdzać na siebie. Samochód oznaczony
                    identyfikatorem
                    <code>X</code>, który
                    musi osiągnąć pole o adresie <code>(5, 3)</code> jednym swoim końcem. Pod uwagę będzie brana
                    również pojedyncza liczba
                    przesunięć każdego samochodu, tj. ruch samochodu w prawo o <code>x</code> pól będzie liczony
                    jako <code>x</code>.
                </div>
            </div>

            <div class="panel panel-primary">
                <div class="panel-heading">Wejściowe dane</div>
                <div class="panel-body">
                    Dane do zadania są przekazywane przez standardowe wejście. <br>
                    Algorytm jest uruchamiany kilkukrotnie z przykładami testowymi o różnej trudności.
                    Pierwsza linia zawiera liczbę <code>T</code> przypadków testowych. Każdy przypadek testowy
                    rozpoczyna się od liczby <code>N</code> samochodów na planszy. Kolejne <code>N</code> lini
                    zawierają opis każdego samochodu w postaci:

                    <pre><code>[id] [start point] [direction] [length]</code></pre>

                    <ul>
                        <li><code>[id]</code> - identyfikator samochodu jako duża litera</li>
                        <li><code>[start point]</code> - punkt znajdujący się najblizej w pionie i poziomie do
                            punktu <code>(0, 0)</code></li>
                        <li><code>[direction]</code> - polozenie samochodu wartosci <code>V</code> albo
                            <code>H</code></li>
                        <li><code>[length]</code> - dlugość samochodu</li>
                    </ul>
                </div>
            </div>

            <div class="panel panel-primary">
                <div class="panel-heading">Wyjściowe dane</div>
                <div class="panel-body">
                    Dla każdego przypadku testowego należy wyświetlić w jednej lini liczbę <code>n</code> kroków
                    oraz <code>n</code> linii ruchów samochodów po planszy w postaci

                    <pre><code>[id] [move direction] [distance]</code></pre>

                    <ul>
                        <li><code>[id]</code> - identyfikator samochodu</li>
                        <li><code>[move direction]</code> - kierunek ruchu samochodu (<code>U</code> - do góry,
                            <code>R</code> - w prawo, <code>D</code> - na dół, <code>L</code> - w lewo)
                        </li>
                        <li><code>[distance]</code> - odległość, na którą należy przesunąć samochód</li>
                    </ul>

                    Liczba kroków oraz kroki powinny zostać przekazana na standardowe wyjście.
                </div>
            </div>

            <div class="panel panel-primary">
                <div class="panel-heading">Przykład</div>
                <div class="panel-body">
                    <h4>Wejście</h4>

                                <pre><code>1
3
X 0 3 H 2
A 4 1 H 2
C 4 2 V 3
                                </code></pre>

                    <h4>Wyjście</h4>
                                <pre><code>3
A L 2
C D 2
X R 4
                                </code></pre>
                </div>
            </div>

            <div class="panel panel-primary">
                <div class="panel-heading">Ograniczenia systemu</div>
                <div class="panel-body">
                    System działa w operacyjnym systemie Linux.
                    Na razie z dostępnych języków są:
                    <ul>
                        <#list runners as runner>
                        <li>${runner.description}</li>
                        </#list>
                    </ul>
                    W przypadku Java, należy przysłać plik, zawierający klasę <code>Main</code> z metodą
                    <code>void main(String[])</code>.
                    <br>
                    Dla rozwiązań w C++ jest używany kompilator GCC wersji 5.3.1 z włączoną flagą
                    <code>-std=c++11</code>
                </div>
            </div>
        </div>

        <div class="col-xs-6">
            <div class="row">
                <div class="panel panel-success">
                    <div class="panel-heading">Wysłanie rozwiązania</div>
                    <div class="panel-body">
                        <div class="alert alert-warning">
                            Uprzejmie proszę o podanie swojego numeru indeksu w tym polu - wyniki będą dostarczone
                            prowadzącemu zgodnie z poadnymi danymi.
                        </div>

                        <form action="/submit" method="post" enctype="multipart/form-data">
                            <div class="form-group">
                                <input type="text" name="author" placeholder="Nr. indeksu" class="form-control"
                                       required="required" pattern="\d{7}" title="Numer indeksu" />
                            </div>

                            <div class="form-group">
                                <select class="form-control" name="language">
                                    <#list runners as runner>
                                    <option value="${runner.acceptedLanguage}">${runner.description}</option>
                                    </#list>
                                </select>
                            </div>

                            <div class="form-group">
                                <input type="file" name="source"/>
                            </div>

                            <div class="row">
                                <div class="col-xs-12 text-center">
                                    <button type="submit" class="btn btn-primary">Wyślij</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="panel panel-info">
                    <div class="panel-heading">Wyniki</div>
                    <div class="panel-body">
                        <div class="alert alert-warning">
                            <strong>Uwaga:</strong> niżej są wymienione tylko poprawne rozwiązania!
                        </div>

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
        </div>
    </div>
</div>
</body>
</html>
