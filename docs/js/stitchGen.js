function genStitchList(pS,pC,pTBC,pTBS,pB,pA)
{
    //const stitchArray = [];
    let stitchString = "";
    let stitchesRequired, maxCrosses, maxTwistsBetweenCrosses, maxTwistsBetweenStitches;
    let twistsBefore, twistsAfter;
    let maxTwistsBefore= 0, maxTwistsAfter= 0;

    // The function can be called with or without attributes.
    // without Number(), document.value is a string. With unexpected results in function genTwists.

    // number of stitches
    if (pS === undefined) {
        stitchesRequired = Number(document.getElementById("stitchesRequired").value);
    } else {
        stitchesRequired = pS;
    }
    // maximum number of crosses
    if (pC === undefined) {
        maxCrosses = Number(document.getElementById("maxCrosses").value);
    } else {
        maxCrosses = pC;
    }
    // maximum number of twists between two crosses
    if (pTBC === undefined) {
        maxTwistsBetweenCrosses = Number(document.getElementById("maxTwistsBetweenCrosses").value);
    } else {
        maxTwistsBetweenCrosses = pTBC;
    }
    // maximum number of twists between two stitches
    if (pTBS === undefined) {
        maxTwistsBetweenStitches = Number(document.getElementById("maxTwistsBetweenStitches").value);
    } else {
        maxTwistsBetweenStitches = pTBS;
    }
    // twists before stitch
    if (pB === undefined) {
        twistsBefore = document.getElementById("twistsBefore").checked;
    } else {
        twistsBefore = pB;
    }
    if (twistsBefore) {maxTwistsBefore = maxTwistsBetweenStitches; }
    // twists after stitch
    if (pA === undefined) {
        twistsAfter = document.getElementById("twistsAfter").checked;
    } else {
        twistsAfter = pA;
    }
    if (twistsAfter) {maxTwistsAfter = maxTwistsBetweenStitches; }

    // validate input - needed if called without arguments.
    if (stitchesRequired < 1)  {        stitchesRequired = 1;     }
    if (stitchesRequired > 25) {        stitchesRequired = 25;    }

    for (let countStitches = 1; countStitches <= stitchesRequired; countStitches++) {
        //stitchArray += genStitch(maxCrosses, maxTwistsBetweenCrosses, maxTwistsBefore, maxTwistsAfter) + "<br>";
        stitchString += genStitch(maxCrosses, maxTwistsBetweenCrosses, maxTwistsBefore, maxTwistsAfter) + "<br>";
    }
    // returning array gives warning about type in getElementById
    return stitchString;
}

function genStitch(maxCrosses, maxTwistsBetweenCrosses, maxTwistsBefore,  maxTwistsAfter)
{
    // define & initialize variables
    let stitch = "";

    // validate input - needed if called with arguments.
    if (maxCrosses < 1) {        maxCrosses = 1;    }
    if (maxCrosses > 5) {        maxCrosses = 5;    }
    if (maxTwistsBetweenCrosses < 1) {        maxTwistsBetweenCrosses = 1;    }
    if (maxTwistsBetweenCrosses > 5) {        maxTwistsBetweenCrosses = 5;    }
    if (maxTwistsBefore < 0) {        maxTwistsBefore = 0;    }
    if (maxTwistsBefore > 5) {        maxTwistsBefore = 5;    }
    if (maxTwistsAfter < 0) {        maxTwistsAfter = 0;    }
    if (maxTwistsAfter > 5) {        maxTwistsAfter = 5;    }

    // how many crosses, minimal 1 cross, therefor add 1 to random integer
    let lengthCrosses = Math.floor(Math.random() * 10000)%maxCrosses + 1;

    // twists before first C
    if (maxTwistsBefore > 0) {
        stitch += genTwists(maxTwistsBefore);
    }

    // generate part of stitch
    for (let countCrosses= 1; countCrosses <= lengthCrosses - 1; countCrosses ++) {
        stitch +="C";
        stitch += genTwists(maxTwistsBetweenCrosses);
    }

    // last Cross
    stitch += "C";

    // twists after last C
    if (maxTwistsAfter > 0) {
        stitch += genTwists(maxTwistsAfter);
    }

    return stitch;
}

// generate a string with "T", "L", "R"
// note: string of 0, 1, ..., maxTwists "L" or "R", therefor modulo (maxTwists + 1)
function genTwists(maxTwists)
{
    let lengthL = Math.floor(Math.random() * 10000)%(maxTwists + 1);
    let lengthR = Math.floor(Math.random() * 10000)%(maxTwists + 1);
    let lengthT = Math.min(lengthL, lengthR);

    lengthL = lengthL-lengthT;
    lengthR = lengthR-lengthT;

    return "T".repeat(lengthT) + "L".repeat(lengthL) + "R".repeat(lengthR);
}

function genVal(vId)
{
    let valWrd = Number(vId.value);
    let vMin = Number(vId.min);
    let vMax = Number(vId.max);

    let toBeep;

    toBeep = isNaN(valWrd);

    if (!toBeep) {
        if (valWrd < vMin) {
            toBeep = true;
            vId.value = vMin;
        }
        if (valWrd > vMax) {
            toBeep = true;
            vId.value = vMax;
        }
    }

    if (toBeep) {
        if (typeof window.AudioContext !== "undefined") {
            const ctx = new window.AudioContext();
            const o = ctx.createOscillator();
            o.type = "sine";
            o.frequency.value = 440;
            o.connect(ctx.destination);
            o.start();
            o.stop(ctx.currentTime + 0.05);
        }
    }

    return vId;
}