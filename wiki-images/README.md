Upload wiki images
==================

If your image was created with some [vector] application, please also upload the source,
thus future contributors won't have to start all over again to improve when you are no longer among us.

Upload for non-techies
----------------------

GitHub doesn't provide a web-interface to upload images into wiki pages.
You can put your images anywhere on the web, and embed them in a wiki page with the toolbar:
![embed]

Leave it to techies to sooner or later integrate copies into the [wiki](https://github.com/d-bl/GroundForge/wiki).

### Don't have a good place to upload?

After completing a few steps you can explore your images and upload new ones at a location like
`https://github.com/<YOUR-ACCOUNT>/GroundForge/blob/patch-1/docs/wiki-images`.
Of course you have to replace the `<YOUR-ACCOUNT>` part
and the patch number might increase under circumstances.
Follow the link to a raw image, that gives you the address to use in the
wiki page you want to embellish with an image.

_The steps_: when signed-in you will find pencil button at the top of [this page].
Make some dummy change, like adding a second empty line somewhere, it won't change the appearance of the page.
Saving the change with `Propose file change` creates a copy under your own account and you are all set.
See also this [help-page], the steps up to 6 should do the job.


Upload for techies
------------------

With proficient GIT(HUB) experience you can upload your images directly
via your local clone of the wiki and push your changes.
Please use a directory for pages with a lot of images.

Please upload `.png` or `.gif` images rather than `.bmp` or `.tiff`.
These are much smaller and thus reduce download time for visitors of the wiki pages.
When non-techies used a too massive or less compatible format, please convert.

### Moderators

When moving images of non-techies into the wiki repo and changing the image links,
please commit [on behalf] of the contributor or document an external source in the commit comment.
Commit corrections of typos and more under your own name.
Close (never merge!) the pull request for images by dropping a link to the page that compares the commit with the previous one.
Start following the fork to not miss any further commits or questions.
Please kindly accept further uploads on a closed pull request,
we don't want to loose contributors by imposing strict procedures on non-techies.

[embed]: https://help.github.com/assets/images/help/wiki/wiki_add_image.png
[help-page]: https://help.github.com/articles/editing-files-in-another-user-s-repository/
[vector]: https://en.wikipedia.org/wiki/Vector_graphics#/media/File:VectorBitmapExample.svg
[this page]: https://github.com/d-bl/GroundForge/blob/master/wiki-images/README.md
[on behalf]: https://stackoverflow.com/questions/18750808/difference-between-author-and-committer-in-git
