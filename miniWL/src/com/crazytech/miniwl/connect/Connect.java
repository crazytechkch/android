package com.crazytech.miniwl.connect;

public class Connect {
	public static final String URL_HOST = "http://phpmysql-crazytechco.rhcloud.com/minibible/";
	
	public class URL{
		public static final String DBDIR = URL_HOST+"uploads/db/miniWLDB";
	}
	
	public class PHP{
		public static final String GET_PUBS_WT = URL_HOST+"get_pubs.php";
		public static final String GET_PUBS_BOOKS = URL_HOST+"php/pubs_books.php";
		public static final String UPLOAD_SERVER_URI = URL_HOST+"php/upload_to_server.php";
		public static final String VERSION = URL_HOST+"php/version.php";
		public static final String UPLOAD = URL_HOST+"php/upload.php";
		public static final String AUTHENTICATE = URL_HOST+"php/authenticate.php";
	}
}
