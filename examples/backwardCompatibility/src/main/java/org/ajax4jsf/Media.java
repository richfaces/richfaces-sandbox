package org.ajax4jsf;

import java.io.IOException;
import java.io.OutputStream;


public interface Media {

	public void paint(OutputStream out, Object data) throws IOException;

	public  void initData();

	public void setData(MediaData data);

	public MediaData getData();

}
