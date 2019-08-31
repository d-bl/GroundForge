---
layout: default
title: Hilfe Intro
---

Hilfe Intro
===========

Experimentieren mit geklöppelte Gründe
--------------------------------------
Indem Sie die Schläge variieren und/oder die Downloads verzerren,
können Sie aus einem einzigen Grund eine Welt unterschiedlicher Gründe zaubern.
Experimentieren und spielen Sie mit Hunderten von vorgefertigte Gründe oder geben Sie Ihre eigenen Muster ein. Auch wenn Sie Räder neu erfinden, können Sie viel spaß haben an eigenen Entdeckungen.

Den [Tiles](https://d-bl.github.io/GroundForge/tiles) (Kacheln) -Seite generiert die Diagramme und listet die wichtigsten Schritte auf. Hilfeseiten am rechten Rand erläutern weitere Details. Leider nur auf Englisch, verwenden bitte Sie allgemeine Online-Übersetzungsseiten und den [OIDFA-Übersetzer] für Klöppelbegriffe.

Nachfolgend finden Sie eine Erklärung für die Verwendung eines vielseitigen Grund.
Nebenbei bemerkt: Auch Bänder entwerfen mit Randschläge gehört zu den Möglichkeiten.

[OIDFA-Übersetzer]: https://www.oidfa.com/translate.html.en


<a name="BK-31"/>

Tutorenkurs 
-----------
Ein [Videoclip](https://github.com/d-bl/GroundForge/releases/download/2019-Q2/catalogues.mp4) (129 MB, 2:28 min)
durchläuft die Herstellung verschiedene Variationen aus einen Grundmusters.
Verwenden Sie die Pause-Taste und den Schieberegler,
um die Schritte zu wiederholen. Probieren Sie es aus,
lassen Sie sich ablenken und haben Sie Spaß damit.

Ein weiterer Videoclip (mit Skript) zeigt einige der wichtigsten Funktionen zum Bearbeiten all dieser Muster. 
Aber zuerst eine kurze Erklärung, was die Aktionen erreichen müssen.

<img src="/GroundForge/help/images/kompakt-31-challenge.png" style="float:right"/>

### Ein vielseitiges Muster - Striche ersetzen / weglassen

Die folgenden statischen Diagramme zeigen vier Variationen
von Tausenden von Möglichkeiten eines vielseitigen Musters.
Für den Grund sind Definitionen von interaktiven Diagrammen "[with]" (mit) und "[without]" (ohne) Randschläge.
Siehe auch die Kommentare zu [Patch-Dimensionen] für die Version mit Randschläge.

Die Auswahl anderer Schläge entspricht dem einfachen Methode von Schläge weglassen.
Um zwei Fliegen mit einer Klappe zu schlagen, teilen wir letztere.

Beachten Sie die grauen Punkte in drei der oberen Diagramme der vier Varianten: einen in der Mitte oder einen oben und unten. Diese grauen Punkte stellen Schläge dar, die leicht weggelassen werden können:
Ersetzen Sie `ctc` durch `-` in den gelben Popups.
Diese Methode kann unerwartete Nebenwirkungen haben, zum Beispiel,
dass mehr [Schläge] verschwinden, oder sie tauscht den Algorithmus aus, um die Paare für den Schlag zu erstellen.
Der letzte Effekt spielt für das Paardiagramm keine Rolle, verursacht aber komische Drahtdiagramme.
![](images/kompakt-31.png)

[Schlag]: #ctc
[Schläge]: #ctc
[without]: https://d-bl.github.io/GroundForge/tiles?patchWidth=19&patchHeight=22&d1=ctct&e2=ct&c2=ct&a2=lct&f3=ctct&d3=ctc&b3=ctct&a3=ct&e4=ctc&c4=ctc&f5=ctc&e5=ctc&d5=ctc&c5=ctc&b5=ctc&a5=ct&e6=ctc&d6=ctc&c6=ctc&f7=ctc&d7=ctc&b7=ctc&a7=rct&e8=ctc&c8=ctc&a8=ct&f9=lctct&d9=ctc&b9=rctct&e10=lct&c10=rct&a10=ct&tile=---5--,d-b-c-,15-5-5,--5-5-,c63532,--158-,ab-5-c,8-5-5-,-5-5-5,b-5-5-&footsideStitch=ctctt&tileStitch=ctc&headsideStitch=ctctt&shiftColsSW=0&shiftRowsSW=10&shiftColsSE=6&shiftRowsSE=5
[with]: https://d-bl.github.io/GroundForge/tiles?patchWidth=7&patchHeight=21&m1=ctcttr&g1=ctct&a1=ctcttl&l2=ctc&k2=ctc&h2=ct&f2=ct&d2=ct&c2=ctc&b2=ctc&l3=ctcrr&k3=ctc&i3=ctct&g3=ctc&e3=ctct&d3=ct&c3=ctc&b3=ctcll&m4=ctcttr&l4=ctc&k4=ctc&h4=ctc&f4=ctc&c4=ctc&b4=ctc&a4=ctcttl&i5=ctc&h5=ctc&g5=ctc&f5=ctc&e5=ctc&d5=ct&h6=ctc&g6=ctc&f6=ctc&m7=ctcttr&l7=ctcrr&k7=ctc&i7=ctcr&g7=ctc&e7=ctcl&d7=ct&c7=ctc&b7=ctcll&a7=ctcttl&l8=ctc&k8=ctc&h8=ctcr&f8=ctcl&d8=ct&c8=ctc&b8=ctc&i9=ctct&g9=ctct&e9=ctct&l10=ctcrr&k10=ctc&h10=ct&f10=ct&d10=ct&c10=ctc&b10=ctcll&footside=b--,xcd,-11,b88,xxx,---,aaa,x78,x--,-aa&tile=---5--,d-b-c-,15-5-5,--5-5-,c63532,--158-,ab-5-c,8-5-5-,-5-5-5,b-5-5-&headside=--C,ABX,88-,11C,XXX,---,DDD,14X,--X,DD-&footsideStitch=ctct&tileStitch=ctc&headsideStitch=ctct&shiftColsSW=0&shiftRowsSW=10&shiftColsSE=6&shiftRowsSE=5
[Patch-Dimensionen]: https://d-bl.github.io/GroundForge/help/Tiles#patch-size

### Auslassen von Schläge (im Abschnitt "Advanced")

Eine andere Methode zum Auslassen von Schläge verwendet den Seitenabschnitt für Benutzter mit einige Erfahrung.
Wenden Sie die blauen Änderungen für die zweite und letzte Variante an.
Die roten Änderungen gelten für die dritte Variante.

![](images/drop-stitches.png)


### Videoclip

Der [Videoclip](https://github.com/d-bl/GroundForge/releases/download/2019-Q2/BK-31.mp4)
(37 MB, weniger als zwei Minuten) folgt dem folgenden Skript.
Verwenden Sie die Pause Taste und den Schieberegler, um die Schritte zu wiederholen.
Ignorieren Sie gegebenenfalls die fett gedruckten fortgeschrittene Schritte.

* 0:00 Beginnt mit dieser Hilfeseite (die Aufnahme zeigt eine ältere Englische Version)
* 0:00 Dem "with"-Link folgen (with = mit)
* 0:05 [Schlag] ersetzten / weggelassen: `ctc` -> `-`
* 0:10 ![wand](../images/wand.png) neue Diagramme generieren
* 0:18 Aktualisieren von die gesamte Seite: Die ursprünglichen Diagramme werden wieder angezeigt
* **0:19** Benutzerbereich rollen nach den teil für Fortgeschrittene Benutzer
* **0:22** entferne den Schlag in der Mitte: `353,153` -> `-5-,5-5-` (Erklärung oben)
* **0:35** ![wand](../images/wand.png) : Unterschiedlicher Prototyp, gleiches Paardiagramm und Fadendiagramm
* 0:41 Folge (i)
* 0:44 Folge "_stitches_" (Schläge)
* 0:45 Kopieren von den Randschlag
* 0:49 Zurück zur vorherigen Browserregisterkarte
* 0:52 Überschreibe den Schlag mit den kopierten variant ein und bearbeite ihn zu einem Umkehrschlag
* 1:19 ![link](../images/link.png)
* 1:23 Aktualisieren der ganze Seite
* 1:26 Jetzt wird das aktuelle Muster neu geladen
* 1:27 Einen Link machen
* 1:33 "_animate_" Paardiagramm (animieren)
* 1:36 Erweitern von den Platz für das Schaltbild (alternativ: ![](../images/size-inc.jpg) ![](../images/size-dec.jpg))
* 1:38 Benutzerbereich weiter nach unten rollen, um das Drahtdiagramm wieder auf dem Bildschirm anzuzeigen
* 1:42 Ändern von die Farbe eines Draht, wenn im Popup die Thread-Nummer angezeigt wird
* 1:45

<a name="ctc"/>

Schläge eingeben
---------------

Ein Schlag wird mit einer Reihe von Buchstaben definiert:

C = cross = Kreuzen<br>
T = twist = Drehen<br>
L = Linkspaar drehen<br>
R = Rechtspaar drehen<br>
P = pin = Nadel (sehr schwach implementiert)<br>
