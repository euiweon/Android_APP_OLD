package com.euiweonjeong.base;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public enum BitmapManager {
	INSTANCE;

	private final Map<String, SoftReference<Bitmap>> cache;

	private final ExecutorService pool;

	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	

	

	private Bitmap placeholder;
	
	private Bitmap placeholder2;

	BitmapManager() {
		cache = new HashMap<String, SoftReference<Bitmap>>();
		pool = Executors.newFixedThreadPool(5);

	}

	public void setPlaceholder(Bitmap bmp) {
		placeholder = bmp;
	}
	
	public void setPlaceholder2(Bitmap bmp) {
		placeholder2 = bmp;
	}

	public Bitmap getBitmapFromCache(String url) {
		if (cache.containsKey(url)) {
			return cache.get(url).get();
		}

		
		return null;
	}
	
	
	public void cacheClear()
	{
		cache.clear();
	}

	public void queueJob(final String url, final ImageView imageView,
			final int width, final int height) {
		/* Create handler in UI thread. */
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String tag = imageViews.get(imageView);
				if (tag != null && tag.equals(url)) {
					if (msg.obj != null) {
						imageView.setImageBitmap((Bitmap) msg.obj);
					} else {
						imageView.setImageBitmap(placeholder);
						Log.d(null, "fail " + url);
					}
				}
			}
		};

		pool.submit(new Runnable() {
			public void run() {
				final Bitmap bmp = downloadBitmap(url, width, height);
				Message message = Message.obtain();
				message.obj = bmp;
				Log.d(null, "Item downloaded: " + url);

				handler.sendMessage(message);
			}
		});
	}
	
	
	public void queueJob_2(final String url, final ImageView imageView) {
		/* Create handler in UI thread. */
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String tag = imageViews.get(imageView);
				if (tag != null && tag.equals(url)) {
					if (msg.obj != null) {
						imageView.setImageBitmap((Bitmap) msg.obj);
					} else {
						imageView.setImageBitmap(placeholder2);
						Log.d(null, "fail " + url);
					}
				}
			}
		};

		pool.submit(new Runnable() {
			public void run() {
				final Bitmap bmp = downloadBitmap2(url);
				Message message = Message.obtain();
				message.obj = bmp;
				Log.d(null, "Item downloaded2: " + url);

				handler.sendMessage(message);
			}
		});
	}
	
	public void queueJob_3(final String url, final ImageView imageView) {
		/* Create handler in UI thread. */
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				String tag = imageViews.get(imageView);
				if (tag != null && tag.equals(url)) {
					if (msg.obj != null) {
						imageView.setImageBitmap((Bitmap) msg.obj);
					} else {
						imageView.setImageBitmap(placeholder2);
						Log.d(null, "fail " + url);
					}
				}
			}
		};

		pool.submit(new Runnable() {
			public void run() {
				final Bitmap bmp = downloadBitmap3(url);
				Message message = Message.obtain();
				message.obj = bmp;
				Log.d(null, "Item downloaded2: " + url);

				handler.sendMessage(message);
			}
		});
	}
	
	
	


	public void loadBitmap(final String url, final ImageView imageView,
			final int width, final int height) {
		imageViews.put(imageView, url);
		Bitmap bitmap = getBitmapFromCache(url);
		imageView.setAdjustViewBounds(true);
		// check in UI thread, so no concurrency issues
		if (bitmap != null) {
			Log.d(null, "Item loaded from cache: " + url);
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setAdjustViewBounds(true);
			imageView.setImageBitmap(placeholder);
			queueJob(url, imageView, width, height);
		}
	}

	public void loadBitmap_2(final String url, final ImageView imageView) {
		imageViews.put(imageView, url);
		Bitmap bitmap = getBitmapFromCache(url);
		imageView.setAdjustViewBounds(true);
		// check in UI thread, so no concurrency issues
		if (bitmap != null) {
			Log.d(null, "Item loaded from cache: " + url);
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setAdjustViewBounds(true);
			imageView.setImageBitmap(placeholder);
			queueJob_2(url, imageView);
		}
	}
	
	public void loadBitmap_3(final String url, final ImageView imageView) {
		imageViews.put(imageView, url);
		Bitmap bitmap = getBitmapFromCache(url);
		imageView.setAdjustViewBounds(true);
		// check in UI thread, so no concurrency issues
		if (bitmap != null) {
			Log.d(null, "Item loaded from cache: " + url);
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setAdjustViewBounds(true);
			imageView.setImageBitmap(placeholder);
			queueJob_3(url, imageView);
		}
	}
	
	
	public Boolean checkCache( final String url)
	{
		Bitmap bitmap = getBitmapFromCache(url);
		if (bitmap != null) 
			return true;
		else
			return false;
	}

	

	private Bitmap downloadBitmap(String url, int width, int height) {
		try {
			
			URL imgUrl = new URL(url );
			URLConnection conn = imgUrl.openConnection();
			conn.connect();
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);
			
			
			Rect rect = new Rect();
			rect.left = 0;
			rect.top = 0;
			rect.bottom = height;
			rect.right = width;
			
			
			
			BitmapFactory.Options bo = new BitmapFactory.Options();
			bo.inSampleSize = 2;
			
			Bitmap bitmap = BitmapFactory.decodeStream(bis, rect,bo);
			
			
			bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
			cache.put(url, new SoftReference<Bitmap>(bitmap));
			return bitmap;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	
	private Bitmap downloadBitmap2(String url) {
		try {
			
			URL imgUrl = new URL(url );
			URLConnection conn = imgUrl.openConnection();
			conn.connect();
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);

			BitmapFactory.Options bo = new BitmapFactory.Options();
			bo.inSampleSize = 2;
			Bitmap bitmap = BitmapFactory.decodeStream(bis, null , bo);

			cache.put(url, new SoftReference<Bitmap>(bitmap));
			return bitmap;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private Bitmap downloadBitmap3(String url) {
		try {
			
			URL imgUrl = new URL(url );
			URLConnection conn = imgUrl.openConnection();
			conn.connect();
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(),512 * 1024);

			BitmapFactory.Options bo = new BitmapFactory.Options();
			Bitmap bitmap = BitmapFactory.decodeStream(bis);

			cache.put(url, new SoftReference<Bitmap>(bitmap));
			return bitmap;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
}

