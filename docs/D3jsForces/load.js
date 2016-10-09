function load() {
  var data = dibl.D3Data().get("586-,-4-5,5-21,-5-7", 12, 9, 1, 1, "checker",
   "A3=tctc,D4=lctc,B4=rctc,A1=ctc,A2=tctc,C1=tctc,D2=rctc,B2=lctc,C3=ctc,C4=tctc")

  diagram.showGraph({
    container: '#pairs',
    nodes: data.pairNodes,
    links: data.pairLinks,
    scale: 0.6,
    transform: 0.6,
    onAnimationEnd: function() { setHref(document.getElementById("dlPair"),'pairs') }
  })
  diagram.showGraph({
    container: '#threads',
    nodes: data.threadNodes,
    links: data.threadLinks,
    scale: 1,
    transform: 1,
    threadColor: '#color',
    palette: 1
  })
}
