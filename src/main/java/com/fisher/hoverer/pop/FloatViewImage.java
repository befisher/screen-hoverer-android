package com.fisher.hoverer.pop;


import com.fisher.hoverer.R;

public class FloatViewImage {
	private static FloatViewImage setting;

	public static FloatViewImage getInstance() {
//		if ( null != setting )
//			return setting;
//		String json = FileUtil.readFileWithoutException( FileUtil.getInternalFile( Setting.FILE_GSON_DATA_FLOAT_IMAGE ) );
//		if ( !"".equals( json ) ) {
//			try {
//				setting = new Gson().fromJson( json, FloatViewImage.class );
//			} catch ( JsonSyntaxException e ) {
//				e.printStackTrace();
//				BugsUtil.onFatalError( "SettingWifi.flush()-> json string format failed[ configure edited ]!" );
//			}
//		}
//		if ( null == setting )
		setting = new FloatViewImage();
		return setting;
	}

	public void flush() {
//		FileUtil.writeFileWithoutException(FileUtil.getInternalFile(Setting.FILE_GSON_DATA_FLOAT_IMAGE), new Gson().toJson(this));
	}

	private int width = 60;
	private int height = 60;
	private int resource = R.mipmap.emoji_glory;
	private double force = 0.02;
	private int top = 6;

	public int getWidth() {
		return width;
	}

	public String getWidthString() {
		return getWidth() + "px";
	}

	public void setWidth(int width) {
		if (this.width == width)
			return;
		this.width = width;
		flush();
	}

	public int getHeight() {
		return height;
	}

	public String getHeightString() {
		return getHeight() + "px";
	}

	public void setHeight(int height) {
		if (this.height == height)
			return;
		this.height = height;
		flush();
	}

	public int getResource() {
		return resource;
	}

	public void setResource(int resource) {
		if (this.resource == resource)
			return;
		this.resource = resource;
		flush();
	}

	public double getForce() {
		return force;
	}

	public void setForce(double force) {
		this.force = force;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}
}
