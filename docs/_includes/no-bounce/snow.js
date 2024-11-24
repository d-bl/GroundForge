function flipToNwSe() {
    document.getElementById("honeycomb_nw_plait").value = flip(
        document.getElementById("honeycomb_ne_plait").value
    )
    document.getElementById("honeycomb_nw_blobs").value =
        getBlobs("honeycomb_ne").map(str => flip(str)).join(",")
    no_bounce_update()
}
function flipToNeSw() {
    document.getElementById("honeycomb_ne_plait").value = flip(
        document.getElementById("honeycomb_nw_plait").value
    )
    document.getElementById("honeycomb_ne_blobs").value =
        getBlobs("honeycomb_nw").map(str => flip(str)).join(",")
    no_bounce_update()
}
function flipDef(idPrefix) {
    let elPlait = document.getElementById(idPrefix + "_plait");
    elPlait.value = flip(elPlait.value)
    document.getElementById(idPrefix + "_blobs").value =
        getBlobs(idPrefix).map(str => flip(str)).join(",")
    no_bounce_update()
}
function flip(plait) {
    return plait.replace(/r/g, "L").replace(/l/g, "R").toLowerCase();
}
function getBlobs(idPrefix) {
    return document.getElementById(idPrefix + "_blobs").value.toLowerCase().split(",");
}
function no_bounce_update() {

    function assignToPlait(prefix, blobs) {
        return blobs.map((item, i) => prefix.replace(/=/g, i + '=') + item).join(",")
    }

    function spiderTips(prefix, blobs) {
        // TODO only if trailing and leading twist, more complicated for square
        const last = blobs[blobs.length - 1]
        return ',' + prefix.replace(/=/g, `${blobs.length - 1}${last.length - 1}=`) + "ctcttctc"
    }

    function getPlait(nrOfBlobs, idPrefix) {
        const start = document.getElementById(idPrefix + "_plait").value.toLowerCase();
        let s = start.repeat(nrOfBlobs).substring(0, nrOfBlobs);
        console.log(`${nrOfBlobs}--${s}--${start}--${idPrefix}`)
        return s;
    }

    const width = document.getElementById("no-bounce-patch-width").value
    const height = document.getElementById("no-bounce-patch-height").value
    const patternType = document.querySelector('input[name="no-bounce-pattern"]:checked').value
    const footsideType = document.querySelector('input[name="no-bounce-footside"]:checked').value

    const blobs = getBlobs('no_bounce');
    const plait = getPlait(blobs.length, "no_bounce")

    // headside columns:
    const g = 'abcdefghijklmnopqrstuvwxyz'[width * 1 + 2]
    const h = 'abcdefghijklmnopqrstuvwxyz'[width * 1 + 3]
    let q = "?"
    switch (patternType) {
        case "diamond":
            q += `b1=${plait}&d1=${plait}&${g}2=${plait}`
            q += "&tile=-5&shiftColsSW=-1&shiftRowsSW=1&shiftColsSE=1&shiftRowsSE=1"
            droste2 = `&droste2=${assignToPlait(`b1=d1=${g}2=`, blobs)}`
            droste3 = '&droste3=ctc' + spiderTips(`b1=d1=${g}2=`, blobs)
            break
        case "square":
            // flip snowflakes on the returning row
            const flippedPlait = flip(plait)
            const flippedBlobs = blobs.map(str => flip(str))
            q += `b1=${plait}&b2=${flippedPlait}&c1=${plait}&c2=${flippedPlait}&${g}1=${plait}&${g}2=${flippedPlait}`
            q += "&tile=8,1&shiftColsSW=0&shiftRowsSW=2&shiftColsSE=1&shiftRowsSE=2"
            droste2 = `&droste2=`
            droste2 += `${assignToPlait(`b1=c1=${g}1=`, blobs)},`
            droste2 += `${assignToPlait(`b2=c2=${g}2=`, flippedBlobs)}`
            droste3 = `&droste3=ctc` + spiderTips(`b1=b2=c1=c2=${g}1==${g}2=`, blobs)
            break
        case "honeycomb":
            const topBlobs = getBlobs('honeycomb_top')
            const neBlobs = getBlobs('honeycomb_ne')
            const nwBlobs = getBlobs('honeycomb_nw')
            const topPlait = getPlait(topBlobs.length, "honeycomb_top")
            const nePlait = getPlait(neBlobs.length, "honeycomb_ne")
            const nwPlait = getPlait(neBlobs.length, "honeycomb_nw")

            q +=  `d1=${plait}&f3=${plait}`
            q += `&d3=${topPlait}&f1=${topPlait}`
            q += `&c2=${nePlait}&e4=${nePlait}`
            q += `&c4=${nwPlait}&e2=${nwPlait}`
            q += `&tile=-5-5,5-5-,-5-5,5-5-,&headside=-c,5-&shiftColsSW=0&shiftRowsSW=4&shiftColsSE=4&shiftRowsSE=4`
            droste2 = `&droste2=`
            droste2 += `${assignToPlait(`d1=${g}2=`, blobs)},`
            droste2 += `${assignToPlait(`f3=${g}2=`, blobs)},`
            droste2 += `${assignToPlait(`d3=${g}2=`, topBlobs)},`
            droste2 += `${assignToPlait(`f1=${g}2=`, topBlobs)},`
            droste2 += `${assignToPlait(`c2=${g}2=`, neBlobs)},`
            droste2 += `${assignToPlait(`e4=${g}2=`, neBlobs)},`
            droste2 += `${assignToPlait(`c4=${g}2=`, nwBlobs)},`
            droste2 += `${assignToPlait(`e2=${g}2=`, nwBlobs)}`
            droste3 = '&droste3=ctc'
            break
    }
    q += "&footside=-5,b-&headside=-c,5-"
    switch (footsideType) {
        case "nothing":
            q += `&a2=-&${h}1=-`
            break
        default:
            q += `&a2=ttt&${h}1=ttt`
            droste2 += `,,a21=${h}10=${g}20=${g}22=tt,${g}12=${g}21=cr,${g}23=r`
            droste3 += `,,b133=ctctttttt,${g}201=${g}210=${g}211=ttttt`
            break
    }
    q += `&patchWidth=${width}&patchHeight=${height}`
    document.getElementById("hex_no_bounce_link").href = "https://d-bl.github.io/GroundForge/droste" + q + droste2 + droste3
    console.log(q)
    console.log(droste2)
    console.log(droste3)
}
