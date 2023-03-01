# Kapitel 4

Endlich sind wir an dem Punkt angekommen, auf den du wahrscheinlich gewartet hast.
Wenn du diesen Punkt übersprungen hast, solltest du wissen, dass ich in diesem Kapitel keine SQL-Abfragen mehr erklären werde.
Alles, was in den vorherigen Kapiteln erklärt wurde, nehme ich als gegeben hin.

**Bitte geh zurück, wenn du etwas nicht verstehst**

In diesem Kapitel dreht sich alles um JDBC und wie wir es nutzen können, um mit unserer Datenbank zu interagieren.
Das heißt, wir werden endlich anfangen, Java zu benutzen und unsere Datenbank über unsere Java-Anwendung abzufragen!
Einige wichtige Begriffe, die du kennen und deren Bedeutung du am Ende des Kapitels verstehen solltest, sind DataSource, jdbc, driver und try with resources.
Der letzte Begriff ist ein allgemeines Konzept von Java, aber wir werden ihn auch hier behandeln, da er für unsere weiteren Schritte entscheidend sein wird.

Wir werden alle Operationen lernen, die wir zuvor in SQL gelernt haben.
Da die Unterschiede zwischen den Datenbanken nun im Allgemeinen geringer sind, werde ich Postgres im Allgemeinen verwenden.
Wenn es Unterschiede gibt, vor allem bei der Implementierung des Treibers selbst, werde ich natürlich trotzdem zeigen, wie es bei jeder Datenbank gemacht wird.
Bedenke nur, dass die Abfragen, die ich zeige, möglicherweise nicht funktionieren, wenn du eine andere Datenbank verwendest.
