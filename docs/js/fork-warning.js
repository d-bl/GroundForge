if (!window.location.host.startsWith('d-dl')) {
    const url = (window.location+'').replace(window.location.host, "d-bl.github.io")
    document.write('<p class=deprecated>This version may be ahead or behind of the <a href="'+url+'">official version</a>.</p>')
}