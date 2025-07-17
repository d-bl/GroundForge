const GF_reflect = {
    svgNS: "http://www.w3.org/2000/svg",

    newSVG(w, h) {
        const svg = document.createElementNS(this.svgNS, "svg");
        svg.setAttribute("width", w);
        svg.setAttribute("height", h);
        svg.setAttribute("xmlns", this.svgNS);

        // show full width of lines on the edge of the viewpBox
        svg.setAttribute("viewBox", "-4 -4 " + (w + 8) + " " + (h + 8));
        return svg;
    },

    createReflections(svgInput) {
        function getDimensions(templateGroup) {
            const linkElements = Array.from(templateGroup.querySelectorAll(".link"));
            let maxKissNr = 0;
            let maxX = 0;
            let maxY = 0;
            linkElements.forEach(element => {
                const dAttr = element.getAttribute("d");
                const xValue = dAttr.replace(/.* /g, '').replace(/,.*/g, '') * 1;
                const yValue = dAttr.replace(/.*,/g, '') * 1;
                if (xValue > maxX) maxX = xValue;
                if (yValue > maxY) maxY = yValue;
                const kissClasses = Array.from(element.classList).filter(cls => cls.startsWith("kiss_"));
                const kissNr = kissClasses[0].replace('kiss_','') * 1 || 0;
                if (kissNr > maxKissNr) maxKissNr = kissNr;
            });
            return [maxX * 3 + 12, maxY * 3 + 12, maxKissNr];
        }

        function copyUploadedTemplate() {
            const twistMarkDefs = svgInput.querySelector("defs");
            const templateGroup = svgInput.getElementById("cloned");
            const [w, h, maxKissNr] = getDimensions(templateGroup);
            const svgDoc = GF_reflect.newSVG(w * 2.3, h *2.3);
            document.body.insertAdjacentHTML("beforeend", "<hr>");
            document.body.appendChild(svgDoc)
            svgDoc.appendChild(twistMarkDefs);
            svgDoc.appendChild(templateGroup);
            return [templateGroup, w, h, svgDoc, maxKissNr];
        }

        function fixKissNr(pathElement, kissNrOffset) {
            const kissClasses = Array.from(pathElement.classList).filter(cls => cls.startsWith("kiss_"));
            if (kissClasses.length > 0) {
                const kissNr = (kissClasses[0].replace('kiss_', ''))*1;
                pathElement.classList.remove(kissClasses[0]);
                pathElement.classList.add(`kiss_${kissNr + kissNrOffset}`);
            }
        }

        function flipPath(d, start, originalWidthOrHeight) {
            // Replace all x,y pairs in path data
            return d.replace(/([MLCQS])([^MLCQS]*)/gi, (match, cmd, coords) => {
                // Split coords into numbers
                let nums = coords.trim().split(/[\s,]+/).map(Number);
                // For commands with x,y pairs, flip x
                if ("MLCQS".includes(cmd.toUpperCase())) {
                    for (let i = start; i < nums.length; i += 2) {
                        nums[i] = originalWidthOrHeight - nums[i];
                    }
                }
                return cmd + " " + nums.join(" ");
            });
        }

        function getTranslate(transform) {
            const match = /translate\(([^,]+),?([^\)]*)\)/.exec(transform || "");
            let tx = 0, ty = 0;
            if (match) {
                tx = parseFloat(match[1]);
                ty = parseFloat(match[2] || 0);
            }
            return {tx, ty};
        }

        function flipGroup(sourceGroup, mode, idPrefix, kissNrOffset) {
            function flipTransform(transform) {
                const {tx, ty} = getTranslate(transform);
                if (transform.includes("scale")) {
                    // createing q from d or p
                    if (mode === "right") {
                        return `translate(${originalWidth - tx},${ty}) scale(-1,-1)`;
                    } else if (mode === "bottom") {
                        return `translate(${tx},${originalHeight - ty}) scale(-1,-1)`;
                    }
                }
                else if (mode === "right") {
                    return `translate(${originalWidth - tx},${ty}) scale(-1,1)`;
                } else if (mode === "bottom") {
                    return `translate(${tx},${originalHeight - ty}) scale(1,-1)`;
                }
                return transform;
            }

            function flipPathD(d) {
                if (mode === "right") {
                    return flipPath(d, 0, originalWidth);
                } else if (mode === "bottom") {
                    return flipPath(d, 1, originalHeight);
                }
                return d;
            }

            function flipTitle(group) {
                const t = group.querySelectorAll('title')[0];
                const stitch = t?.textContent?.toLowerCase();
                console.log(mode, stitch, group.id)
                if (mode === "bottom" && stitch) {
                    t.textContent = stitch.split('').reverse().join('');
                } else if (mode === "right" && stitch) {
                    t.textContent = stitch.replace(/l/g, "R").replace(/r/g, "l").replace(/R/g, "r");
                }
            }

            const reflection = document.createElementNS(GF_reflect.svgNS, "g");
            reflection.id = idPrefix + "Reflection";
            svgDoc.appendChild(reflection);

            Array.from(sourceGroup.children).forEach(child => {
                const clone = child.cloneNode(true);
                // TODO fix _at_ classes of paths and IDs of groups
                if (clone.tagName === "g") {
                    flipTitle(clone);
                    const origTransform = clone.getAttribute("transform");
                    clone.setAttribute("transform", flipTransform(origTransform));
                    clone.id = idPrefix + clone.id;
                } else if (clone.tagName === "path") {
                    // IDs on paths may not be unique, keeping them might lead to confusion
                    child.removeAttribute("id");
                    clone.removeAttribute("id");
                    clone.classList.forEach(cls => {
                            if (cls.includes("_at_") && !cls.includes("_at_" + idPrefix)) {
                                clone.classList.remove(cls);
                                clone.classList.add(cls.replace("_at_", "_at_" + idPrefix));
                            }
                        }
                    );
                    fixKissNr(clone, kissNrOffset);
                    const origD = clone.getAttribute("d");
                    clone.setAttribute("d", flipPathD(origD));
                }
                reflection.appendChild(clone);
            });
            return reflection;
        }
        // clear previous uploads
        for (const el of document.querySelectorAll("svg,hr")) {
            el.remove();
        }

        const [bReflection, originalWidth, originalHeight, svgDoc, mmxKissNr] = copyUploadedTemplate();
        const dReflection = flipGroup(bReflection, "right", 'd', mmxKissNr + 1);
        const pReflection = flipGroup(bReflection, "bottom", 'p', (mmxKissNr + 1) * 2);
        const qReflection = flipGroup(dReflection, "bottom", 'q', (mmxKissNr + 1) * 2);
        bReflection.setAttribute("transform", "scale(3)");
        dReflection.setAttribute("transform", `scale(3) translate(${-originalWidth/4},0)`);
        pReflection.setAttribute("transform", `scale(3) translate(0,${-originalHeight/4})`);
        qReflection.setAttribute("transform", `scale(3) translate(${-originalWidth/4},${-originalHeight/4})`);
    },

    async readSVGFile(file) {
        if (!file) {
            alert("Failed to readd file" + file.name);
            return;
        }
        const svgContent = await file.text();
        const sanitized = DOMPurify.sanitize(svgContent, { USE_PROFILES: { svg: true, svgFilters: true } });
        return new DOMParser().parseFromString(sanitized, "image/svg+xml");
    },

    async init() {
        document.getElementById("upload").addEventListener("change", async event => {
            const file = event.target.files[0];
            this.createReflections(await this.readSVGFile(file));
        });

        try {
            const response = await fetch('../p2t/demo.svg');
            const svgContent = await response.text();
            const file = new File([svgContent], "demo.svg", {type: "image/svg+xml"});
            this.createReflections(await this.readSVGFile(file));
        } catch (error) {
            console.error("Error loading the file:", error);
        }
    }
}