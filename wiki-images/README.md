Upload wiki images
==================

GitHub doesn't provide a web-interface to upload images into wiki pages.
You can put them anywhere on the web, use them and leave it to techies
to sooner or later integrate copies into the wiki.

If your image was created with some [vector] application, please also upload the source,
thus future contributors won't have to start all over again to improve when you are no longer among us.


Don't have a good place to upload?
----------------------------------

A few steps creates your own upload location and establish a communication channel with the moderators.
After completing these steps you can explore your images and upload new ones at a location like
`https://github.com/<YOUR-ACCOUNT>/GroundForge/blob/patch-1/docs/wiki-images`.
Of course you have to replace the `<YOUR-ACCOUNT>` part
and the patch number might increase under circumstances.

_The steps_: when signed-in you will find an upload button at the top of [this page].
Use it to add your images and follow the green buttons to
propose a change and create a pull request. You are all set now.
Follow the link to the raw image, that gives you the address to use in the
wiki page you want to embellish with an image.

A techie will move your images on your behalf into the repository of the wiki
and communicate with you about that process.


Upload for techies
------------------

With proficient GIT(HUB) experience you can upload your images directly
via your local clone of the wiki and push your changes.
Please use a directory for pages with a lot of images.

Please upload `.png` or `.gif` images rather than `.bmp` or `.tiff`.
These are much smaller and thus reduce download time for visitors of the wiki pages.
When non-techies used a too massive or less compatible format, please convert.

When moving images of non-techies into the wiki repo and changing the image links,
please commit [on behalf] of the contributor or document an external source in the commit comment.
Commit corrections of typos and more under your own name.
Close (never merge!) the pull request for images by dropping a link to the page that compares the commit with the previous one.
Start following the fork to not miss any further commits or questions.
Please kindly accept further uploads on a closed pull request,
we don't want to loose contributors by imposing strict procedures on non-techies.

[vector]: https://en.wikipedia.org/wiki/Vector_graphics#/media/File:VectorBitmapExample.svg
[this page]: https://github.com/d-bl/GroundForge/tree/master/wiki-images
[on behalf]: https://stackoverflow.com/questions/18750808/difference-between-author-and-committer-in-git