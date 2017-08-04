Incubation chamber for wiki images
==================================

GitHub doesn't provide a a web-interface to upload images into wiki pages.
This directory serves as temporary location for images uploaded by non-techies.

Please don't upload `.bmp` or `.tiff` images but use `.png` or `.gif`.
These are much smaller and thus reduce download time for visitors of the wiki pages.

If your image was created with some vector application, please also upload the source,
thus future contributors won't have to start all over again to improve when you are no longer among us.


Non-techies
-----------

At the top of [this page] you will find an upload button.
Use it to add your images and follow the instructions to
propose your change and create a pull request.
You can explore your uploaded images at a location like
`https://github.com/<YOUR-ACCOUNT>/GroundForge/blob/patch-1/docs/wiki-images`.
Of course you have to replace the `<YOUR-ACCOUNT>` part and the patch number might increase.
Follow the link to the raw image, that gives you the address to use in the
wiki page you want to embellish with an image.

A techie will move your images on your behalf into the repository of the wiki
and communicate with you about that process.


Techies
-------

With proficient GIT experience you can upload your own images directly
via your local clone of the wiki and push your changes.
Please use a directory for pages with a lot of images.

When moving images of non-techies into the wiki repo and changing the image links,
please commit [on behalf] of the contributor.
Commit corrections of typos and more under your own name.
Close (never merge!) the pull request by dropping a link to the page that compares the commit with the previous one.
Start following the fork to not miss any further commits or questions.
Please kindly accept further uploads on a closed pull request,
we don't want to loose contributors by imposing strict procedures on non-techies.

[this page]: https://github.com/d-bl/GroundForge/tree/master/wiki-images
[on behalf]: https://stackoverflow.com/questions/18750808/difference-between-author-and-committer-in-git