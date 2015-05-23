# SweetSpot

Android gallery application showing images in aspect correct
ratio using Android Recycler view with a StaggeredLayoutManager.

Day dividers are grouping the collection.

Using Picasso for image decoding with a DiskCache added to it. No 
hard coding of image sizes is made but instead letting Picasso
and and diskcache to take whatever the bitmap gets to be. This
way the layout can be changed in whatever way without needing 
to think about image size quality. For example the vertical recycler
used in the main grid is the same component as the horizontal
carousel in Fullscreen.

Using Android LoaderManager and loaders for adapter loading through
a AsyncTaskLoader loading 'collections'. The loader can be configured
with a 'modifier' which can alter the raw cursor of photos. For
example by insering day dividers into the collection. Using
RxJava flatmap for this. The AsyncTaskLoader registers a 
ContentObserver on the cursor for MediaStore events.

Clients can subscribe to adapter item clicks through 
RxJava PublishSubject.

The ImageView is extended to enforce aspect ratio dimensions
through onMeasure.

There is a separate Recycler view layoutmanager for the 
carousel where the smoothScroll is modified for slower scrolling.

When swiping images in Fullscreen, the carousel follows along 
with current image white tinted.

The main application initializes Picasso and DiskCache singletons.

When pressing an image, a second activity, FullscreenActivity is 
launched. It consists of a v4 view pager and a carousel view at the
botton (recycler view). One can hide or show the carousel.

