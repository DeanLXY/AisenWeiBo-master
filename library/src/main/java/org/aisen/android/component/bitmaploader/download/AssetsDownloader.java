package org.aisen.android.component.bitmaploader.download;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.aisen.android.common.context.GlobalContext;
import org.aisen.android.component.bitmaploader.core.ImageConfig;

public class AssetsDownloader implements Downloader {

	@Override
	public byte[] downloadBitmap(String url, ImageConfig config) throws Exception {
		try {
			DownloadProcess progress = config.getProgress();

			if (progress != null)
				progress.sendPrepareDownload(url);

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			// 加载assets目录
			InputStream in = GlobalContext.getInstance().getAssets().open(url);

			if (progress != null)
				progress.receiveLength(in.available());

			if (in == null)
				return null;

			// 获取图片数据
			byte[] buffer = new byte[1024 * 128];
			int readLen = -1;
			int readBytes = 0;
			while ((readLen = in.read(buffer)) != -1) {
				readBytes += readLen;
				if (progress != null)
					progress.sendProgress(readBytes);
				out.write(buffer, 0, readLen);
			}

			byte[] bs = out.toByteArray();
			in.close();
			out.close();

			return bs;
		} catch (Exception e) {
			if(config.getProgress()!=null)
				config.getProgress().sendException(e);
			throw new Exception(e.getCause());
		}
	};

}
