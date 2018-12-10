package com.hz.demo.core;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;


public class ResponseUtil {

	private static final Log log = LogFactory.getLog(ResponseUtil.class);

	private final static String CONTEXTTYPE_XLS = ".xls";
	private final static String CONTEXTTYPE_XLSX = ".xlsx";
	private final static String CONTEXTTYPE_DOC = ".doc";
	private final static String CONTEXTTYPE_DOT = ".dot";
	private final static String CONTEXTTYPE_RTF = ".rtf";
	private final static String CONTEXTTYPE_WIZ = ".wiz";
	private final static String CONTEXTTYPE_DOCX = ".docx";
	private final static String CONTEXTTYPE_DOTX = ".dotx";
	private final static String CONTEXTTYPE_PPT = ".ppt";
	private final static String CONTEXTTYPE_PPS = ".pps";
	private final static String CONTEXTTYPE_PPA = ".ppa";
	private final static String CONTEXTTYPE_POT = ".pot";
	private final static String CONTEXTTYPE_PPTX = ".pptx";
	private final static String CONTEXTTYPE_PPSX = ".ppsx";
	private final static String CONTEXTTYPE_POTX = ".potx";
	private final static String CONTEXTTYPE_PDF = ".pdf";
	private final static String CONTEXTTYPE_TXT = ".txt";
	private final static String CONTEXTTYPE_SOL = ".sol";
	private final static String CONTEXTTYPE_SOR = ".sor";
	private final static String CONTEXTTYPE_MP3 = ".mp3";
	private final static String CONTEXTTYPE_MP4 = ".mp4";
	private final static String CONTEXTTYPE_M4E = ".m4e";
	private final static String CONTEXTTYPE_EPUB = ".epub";
	private final static String CONTEXTTYPE_JPG = ".jpg";
	private final static String CONTEXTTYPE_JPEG = ".jpeg";
	private final static String CONTEXTTYPE_GIF = ".gif";
	private final static String CONTEXTTYPE_PNG = ".png";
	private final static String CONTEXTTYPE_BMP = ".bmp";

	private final static String APPLICATION_XLS = "application/vnd.ms-excel";
	private final static String APPLICATION_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	private final static String APPLICATION_DOC = "application/msword";
	private final static String APPLICATION_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	private final static String APPLICATION_DOTX = "application/vnd.openxmlformats-officedocument.wordprocessingml.template";
	private final static String APPLICATION_PPT = "application/vnd.ms-powerpoint";
	private final static String APPLICATION_PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
	private final static String APPLICATION_PPSX = "application/vnd.openxmlformats-officedocument.presentationml.slideshow";
	private final static String APPLICATION_POTX = "application/vnd.openxmlformats-officedocument.presentationml.template";
	private final static String APPLICATION_PDF = "application/pdf";
	private final static String APPLICATION_TXT = "text/plain";
	private final static String APPLICATION_MP3 = "audio/mp3";
	private final static String APPLICATION_MP4 = "video/mpeg4";
	private final static String APPLICATION_EPUB = "application/epub+zip";
	private final static String APPLICATION_JPG = "image/jpeg";
	private final static String APPLICATION_GIF = "image/gif";
	private final static String APPLICATION_PNG = "image/png";
	private final static String APPLICATION_BMP = "application/x-bmp";
	private final static String APPLICATION_OTHER = "application/x-msdownload";

	private static final int BLOCKSIZE = 8192;


	/**
	 * 根据文件名得到Response的ContextType
	 *
	 * @param fileName
	 * @return
	 */
	public static String getContextType(String fileName) {
		String fileType = "." + StringUtils.substringAfterLast(fileName, ".");
		if (CONTEXTTYPE_XLS.equalsIgnoreCase(fileType)) {
			return APPLICATION_XLS;
		} else if (CONTEXTTYPE_XLSX.equalsIgnoreCase(fileType)) {
			return APPLICATION_XLSX;
		} else if (CONTEXTTYPE_DOC.equalsIgnoreCase(fileType)
				|| CONTEXTTYPE_DOT.equalsIgnoreCase(fileType)
				|| CONTEXTTYPE_RTF.equalsIgnoreCase(fileType)
				|| CONTEXTTYPE_WIZ.equalsIgnoreCase(fileType)) {
			return APPLICATION_DOC;
		} else if (CONTEXTTYPE_DOCX.equalsIgnoreCase(fileType)) {
			return APPLICATION_DOCX;
		} else if (CONTEXTTYPE_DOTX.equalsIgnoreCase(fileType)) {
			return APPLICATION_DOTX;
		} else if (CONTEXTTYPE_PPT.equalsIgnoreCase(fileType)
				|| CONTEXTTYPE_PPS.equalsIgnoreCase(fileType)
				|| CONTEXTTYPE_PPA.equalsIgnoreCase(fileType)
				|| CONTEXTTYPE_POT.equalsIgnoreCase(fileType)) {
			return APPLICATION_PPT;
		} else if (CONTEXTTYPE_PPTX.equalsIgnoreCase(fileType)) {
			return APPLICATION_PPTX;
		} else if (CONTEXTTYPE_PPSX.equalsIgnoreCase(fileType)) {
			return APPLICATION_PPSX;
		} else if (CONTEXTTYPE_POTX.equalsIgnoreCase(fileType)) {
			return APPLICATION_POTX;
		} else if (CONTEXTTYPE_PDF.equalsIgnoreCase(fileType)) {
			return APPLICATION_PDF;
		} else if (CONTEXTTYPE_TXT.equalsIgnoreCase(fileType)
				|| CONTEXTTYPE_SOL.equalsIgnoreCase(fileType)
				|| CONTEXTTYPE_SOR.equalsIgnoreCase(fileType)) {
			return APPLICATION_TXT;
		} else if (CONTEXTTYPE_MP3.equalsIgnoreCase(fileType)) {
			return APPLICATION_MP3;
		} else if (CONTEXTTYPE_MP4.equalsIgnoreCase(fileType)
				|| CONTEXTTYPE_M4E.equalsIgnoreCase(fileType)) {
			return APPLICATION_MP4;
		} else if (CONTEXTTYPE_EPUB.equalsIgnoreCase(fileType)) {
			return APPLICATION_EPUB;
		} else if (CONTEXTTYPE_JPG.equalsIgnoreCase(fileType)
				|| CONTEXTTYPE_JPEG.equalsIgnoreCase(fileType)) {
			return APPLICATION_JPG;
		} else if (CONTEXTTYPE_GIF.equalsIgnoreCase(fileType)) {
			return APPLICATION_GIF;
		} else if (CONTEXTTYPE_PNG.equalsIgnoreCase(fileType)) {
			return APPLICATION_PNG;
		} else if (CONTEXTTYPE_BMP.equalsIgnoreCase(fileType)) {
			return APPLICATION_BMP;
		} else {
			return APPLICATION_OTHER;
		}
	}

	/**
	 * 得到Response的下载输出流
	 *
	 * @param response
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static OutputStream getDownLoadOutStream(
			HttpServletResponse response, String fileName) throws IOException {
		String context = getContextType(fileName);
		// 设置文件类型
		response.setContentType(context);

		// 设置头文件
		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ new String(fileName.getBytes(), "ISO8859-1") + "\"");

		return response.getOutputStream();
	}



	/**
	 * 预览文件,根据文件IO
	 *
	 * @param response
	 * @param downloadFile
	 * @throws IOException
	 */
	public static void previewFile(HttpServletResponse response,
			File downloadFile, String downloadName) throws IOException {
		OutputStream out = response.getOutputStream();
		outputStream(downloadFile, out);
		downloadFile = null;
	}

	/**
	 * 下载文件,根据文件路径
	 *
	 * @param response
	 * @param filePath
	 * @throws Exception
	 */
	public static void downloadFile(HttpServletResponse response,
			String filePath) throws Exception {
		File downloadFile = new File(filePath);
		downloadFile(response, downloadFile);
	}



	/**
	 * 下载文件,根据文件IO
	 *
	 * @param response
	 * @param downloadFile
	 * @throws IOException
	 */
	public static void downloadFile(HttpServletResponse response,
			File downloadFile) throws IOException {
		OutputStream out = getDownLoadOutStream(response, downloadFile
				.getName());
		outputStream(downloadFile, out);
	}


	/**
	 * 下载文件,根据文件IO
	 *
	 * @param response
	 * @param downloadFile
	 * @throws IOException
	 */
	public static void downloadFile(HttpServletResponse response,
			File downloadFile, String downloadName) throws IOException {
		OutputStream out = getDownLoadOutStream(response, downloadName);
		outputStream(downloadFile, out);
	}

	/**
	 * 输出文件流
	 *
	 * @param outFile
	 * @param out
	 * @throws IOException
	 */
	public static void outputStream(File outFile, OutputStream out)
			throws IOException {
		if (!outFile.exists()) {
			throw new FileNotFoundException(outFile.getPath());
		}
		if (out == null) {
			throw new NullPointerException("outputStream is null!");
		}
		InputStream fileIn = new FileInputStream(outFile);

		outputStream(fileIn, outFile.length(), out);
	}

	/**
	 * 输入流
	 *
	 * @param in
	 * @param inLen
	 * @param out
	 * @throws IOException
	 */
	public static void outputStream(InputStream in, long inLen, OutputStream out)
			throws IOException {
		int readBytes = 0;
		int totalRead = 0;
		byte b[] = new byte[BLOCKSIZE];

		try {
			while (totalRead < inLen) {
				readBytes = in.read(b, 0, BLOCKSIZE);
				totalRead += readBytes;
				out.write(b, 0, readBytes);
			}
			out.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			in.close();
			out.close();
		}
	}
}
