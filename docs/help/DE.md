---
layout: default
title: Hilfe Intro
lang: de
ref: intro
---

Hilfe Intro
===========

Spielen mit geklöppelten Gründen
------------------------------
Indem Sie die Schläge variieren und/oder die Zeichnungen verzerren,
können Sie aus einem einzigen Grund unzählige unterschiedliche Gründe zaubern.
Experimentieren und spielen Sie mit Hunderten von vorgefertigten Gründen oder geben Sie Ihre eigenen Muster ein. Auch wenn Sie alte Gründe neu erfinden, können Sie viel Spaß haben an eigenen Entdeckungen.

Die [Tiles](/GroundForge/tiles) (Kacheln)-Seite 
generiert die Diagramme der Gründe. Die Seite führt Sie mit einer kurzen Liste durch die wichtigsten Schritte. 
Links am rechten Rand erläutern weitere Details,
leider nur auf Englisch, verwenden Sie bitte allgemeine Online-Übersetzungsseiten 
und den [OIDFA-Übersetzer] für Klöppelbegriffe.

Nachfolgend finden Sie eine Erklärung für die Verwendung eines vielseitigen Grundes.
Nebenbei bemerkt: auch Bänder entwerfen mit Randschlägen gehört zu den Möglichkeiten.

[OIDFA-Übersetzer]: https://www.oidfa.com/translate.html.en


<a name="BK-31"/>

Tutorenkurs 
-----------
Den ersten [Videoclip](https://github.com/d-bl/GroundForge/releases/download/2019-Q2/catalogues.mp4) (129 MB, 2:28 min)
sucht ein Grundmuster in verschiedenen Sammlungen.
Dieses Gründe werden auf verschiedene Arten präsentiert oder benutzten Variationen in den Schlägen.
Verwenden Sie die Pause-Taste und den Schieberegler,
um die Schritte zu wiederholen. Probieren Sie es aus,
lassen Sie sich verführen und haben Sie Spaß damit.

Ein weiterer Videoclip (mit Skript) zeigt einige der wichtigsten Funktionen zum Bearbeiten all dieser Muster. 
Aber zuerst eine kurze Erklärung, worum es bei den Aktionen geht.

### Ein Muster mit viele Möglichkeiten - Schläge ersetzen / weglassen

<img src="/GroundForge/help/images/kompakt-31-challenge.png" style="float:right"/>
Rechts eine Arbeitszeichnung für einen traditionellen Binche-Grund. 
Kleine Änderungen erlauben tausende Variationen. 
Unten werden nur vier Variationen gezeigt, jeweils mit einem Prototyp und einem Faden-Diagramm. 
Für den Prototyp wird die Arbeitszeichnung verzerrt bis die Schläge auf die Rasterpunkte zugreifen. 
Die Ziffern und Buchstaben bestimmen, welche Rasterpunkte verbunden werden müssen. 
Die Software wandelt diese Ziffern über Arbeitszeichnungen in Faden-Diagramme um.
Zum Start gibt es zwei interaktive Diagramme:  "[with]" (mit) und "[without]" (ohne) Randschläge.
Siehe auch die Kommentare zu [Patch-Dimensionen] für die Version mit Randschläge.

Schläge verändern erfordert dasselbe Vorgehen wie das Weglassen von Schlägen mit der einfachen Methode.
Um zwei Fliegen mit einer Klappe zu schlagen, zeigen wir Letzteres.

Beachten Sie die grauen Punkte in drei oberen Diagrammen der vier Varianten: 
einen in der Mitte oder einen oben und unten. 
Diese grauen Punkte stellen Schläge dar, die leicht weggelassen werden können:
Ersetzen Sie `ctc` durch `-` in den gelben popups.
Diese Methode kann unerwartete Nebenwirkungen haben, zum Beispiel,
dass mehr [Schläge] verschwinden, oder der Algorithmus tauscht die Paare bevor er einen Schlag erstellt.
Der letzte Effekt spielt für das Paar-Diagramm keine Rolle, verursacht aber merkwürdige Faden-Diagramme.
![](images/kompakt-31.png)

[Schlag]: #ctc
[Schläge]: #ctc
[without]: /GroundForge/tiles?patchWidth=19&patchHeight=22&d1=ctct&e2=ct&c2=ct&a2=lct&f3=ctct&d3=ctc&b3=ctct&a3=ct&e4=ctc&c4=ctc&f5=ctc&e5=ctc&d5=ctc&c5=ctc&b5=ctc&a5=ct&e6=ctc&d6=ctc&c6=ctc&f7=ctc&d7=ctc&b7=ctc&a7=rct&e8=ctc&c8=ctc&a8=ct&f9=lctct&d9=ctc&b9=rctct&e10=lct&c10=rct&a10=ct&tile=---5--,d-b-c-,15-5-5,--5-5-,c63532,--158-,ab-5-c,8-5-5-,-5-5-5,b-5-5-&footsideStitch=ctctt&tileStitch=ctc&headsideStitch=ctctt&shiftColsSW=0&shiftRowsSW=10&shiftColsSE=6&shiftRowsSE=5
[with]: /GroundForge/tiles?patchWidth=7&patchHeight=21&m1=ctcttr&g1=ctct&a1=ctcttl&l2=ctc&k2=ctc&h2=ct&f2=ct&d2=ct&c2=ctc&b2=ctc&l3=ctcrr&k3=ctc&i3=ctct&g3=ctc&e3=ctct&d3=ct&c3=ctc&b3=ctcll&m4=ctcttr&l4=ctc&k4=ctc&h4=ctc&f4=ctc&c4=ctc&b4=ctc&a4=ctcttl&i5=ctc&h5=ctc&g5=ctc&f5=ctc&e5=ctc&d5=ct&h6=ctc&g6=ctc&f6=ctc&m7=ctcttr&l7=ctcrr&k7=ctc&i7=ctcr&g7=ctc&e7=ctcl&d7=ct&c7=ctc&b7=ctcll&a7=ctcttl&l8=ctc&k8=ctc&h8=ctcr&f8=ctcl&d8=ct&c8=ctc&b8=ctc&i9=ctct&g9=ctct&e9=ctct&l10=ctcrr&k10=ctc&h10=ct&f10=ct&d10=ct&c10=ctc&b10=ctcll&footside=b--,xcd,-11,b88,xxx,---,aaa,x78,x--,-aa&tile=---5--,d-b-c-,15-5-5,--5-5-,c63532,--158-,ab-5-c,8-5-5-,-5-5-5,b-5-5-&headside=--C,ABX,88-,11C,XXX,---,DDD,14X,--X,DD-&footsideStitch=ctct&tileStitch=ctc&headsideStitch=ctct&shiftColsSW=0&shiftRowsSW=10&shiftColsSE=6&shiftRowsSE=5
[Patch-Dimensionen]: /GroundForge/help/Patch-Size

### Auslassen von Schlägen (im Abschnitt "Advanced")

Eine andere Methode zum Auslassen von Schlägen findet sich im Kapitel
"Forms for advanced users" (Benutzer mit einiger Erfahrung).
Wenden Sie die blauen Änderungen für die zweite und letzte Variante an.
Die roten Änderungen gelten für die dritte Variante.

![](images/drop-stitches.png)


### Videoclip

Der [Videoclip](https://github.com/d-bl/GroundForge/releases/download/2019-Q2/BK-31.mp4)
(37 MB, weniger als zwei Minuten) folgt dem Skript unten.
Verwenden Sie die Pausen-Taste und den Schieberegler, um die Schritte zu wiederholen.
Ignorieren Sie gegebenenfalls die fett gedruckten fortgeschrittenen Schritte.

* 0:00 Beginnt mit dieser Hilfeseite (die Aufnahme zeigt eine ältere Englische Version)
* 0:00 Dem "with"-Link folgen (with = mit)
* 0:05 [Schlag] ersetzten / weggelassen: `ctc` -> `-`
* 0:10 ![wand](../images/wand.png) neue Diagramme generieren
* 0:18 Aktualisieren der gesamten Seite: Die ursprünglichen Diagramme werden wieder angezeigt
* **0:19** Benutzerbereich scrollen zum Teil für fortgeschrittene Benutzer
* **0:22** entferne den Schlag in der Mitte: `353,153` -> `-5-,5-5-` (Erklärung oben)
* **0:35** ![wand](../images/wand.png) : Unterschiedlicher Prototyp, gleiches Paardiagramm und Faden-Diagramm
* 0:41 Folge (i)
* 0:44 Folge "_stitches_" (Schläge)
* 0:45 Kopieren vom Randschlag
* 0:49 Zurück zur vorherigen Browser-Registerkarte
* 0:52 Überschreibe den Schlag mit der kopierten Variante und bearbeite ihn zu einem Umkehrschlag
* 1:19 ![link](../images/link.png)
* 1:23 Aktualisieren der ganzen Seite
* 1:26 Jetzt wird das aktuelle Muster neu geladen
* 1:27 Einen Link machen
* 1:33 "_animate_" Paardiagramm (animieren)
* 1:36 Erweiterung des Platzes für das Schaltbild (alternativ: ![](../images/size-inc.jpg) ![](../images/size-dec.jpg))
* 1:38 Benutzerbereich weiter nach unten scrollen, um das Faden-Diagramm wieder auf dem Bildschirm anzuzeigen
* 1:42 Ändern der Farbe eines Fades, wenn im popup die Faden-Nummer angezeigt wird
* 1:45

<a name="ctc"/>

Schläge eingeben
---------------

Ein Schlag wird mit einer Reihe von Buchstaben definiert:

C = cross = Kreuzen<br>
T = twist = Drehen<br>
L = linkes Paar drehen<br>
R = rechtes Paar drehen<br>
P = pin = Nadel (sehr schwach implementiert)<br>
