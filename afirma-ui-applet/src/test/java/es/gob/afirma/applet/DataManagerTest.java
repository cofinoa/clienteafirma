/* Copyright (C) 2011 [Gobierno de Espana]
 * This file is part of "Cliente @Firma".
 * "Cliente @Firma" is free software; you can redistribute it and/or modify it under the terms of:
 *   - the GNU General Public License as published by the Free Software Foundation;
 *     either version 2 of the License, or (at your option) any later version.
 *   - or The European Software License; either version 1.1 or (at your option) any later version.
 * Date: 11/01/11
 * You may contact the copyright holder at: soporte.afirma5@mpt.es
 */

package es.gob.afirma.applet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

import junit.framework.Assert;

import org.junit.Test;

import es.gob.afirma.core.misc.AOUtil;
import es.gob.afirma.core.misc.Base64;

public class DataManagerTest {

	private static final String DATA_FILE = "txt"; //$NON-NLS-1$

	private static final String DATA_FILE_URI;

	private static final byte[] DATA;

	private static final String DATA_BASE64;

	private static final String DATA_HASH_BASE64;

	private static final String TEXT;

	static {

		DATA_FILE_URI = DataManagerTest.class.getResource("/" + DATA_FILE).toString();

		final InputStream isData = DataManagerTest.class.getResourceAsStream("/" + DATA_FILE);
		byte[] data;
		try {
			data = AOUtil.getDataFromInputStream(isData);
		} catch (final IOException e) {
			data = null;
		}
		Assert.assertNotNull("No se han cargado los datos", data); //$NON-NLS-1$
		Assert.assertTrue(data != null && data.length > 0);
		DATA = data;

		DATA_BASE64 = Base64.encode(DATA);

		byte[] hash;
		try {
			hash = CryptoUtils.getMessageDigest(DATA, "SHA-1"); //$NON-NLS-1$
		} catch (final NoSuchAlgorithmException e) {
			hash = null;
		}
		DATA_HASH_BASE64 = Base64.encode(hash);
		Assert.assertNotNull("No se ha calculado el hash", DATA_HASH_BASE64);

		TEXT = new String(DATA);
	}


	@Test
	public void recoverData() {

		final SignApplet applet = new SignApplet();
		String dataB64;
		String fileUri;
		String text;

		applet.setData(DATA_BASE64);
		dataB64 = applet.getB64Data();
		Assert.assertEquals(DATA_BASE64, dataB64);
		Assert.assertEquals("", applet.getFileUsedPath()); //$NON-NLS-1$

		applet.setData(DATA_BASE64);
		text = applet.getData();
		Assert.assertEquals(TEXT, text);

		applet.setFileuri(DATA_FILE_URI);
		fileUri = applet.getFileUsedPath();
		Assert.assertEquals(DATA_FILE_URI, fileUri);
		Assert.assertEquals("", applet.getData()); //$NON-NLS-1$

		applet.setFileuri(DATA_FILE_URI);
		dataB64 = applet.getFileBase64Encoded(false);

		Assert.assertEquals(DATA_BASE64, dataB64);

		System.out.println("Ruta fichero: " + new File(DATA_FILE_URI).getAbsolutePath()); //$NON-NLS-1$
		applet.setFileuri(DATA_FILE_URI);
		dataB64 = applet.getFileBase64Encoded(false);
		Assert.assertEquals(DATA_BASE64, dataB64);

		dataB64 = applet.getFileBase64Encoded(DATA_FILE_URI, false);
		Assert.assertEquals(DATA_BASE64, dataB64);

		applet.setFileuri(DATA_FILE_URI);
		Assert.assertEquals(DATA_HASH_BASE64, applet.getFileHashBase64Encoded());

		Assert.assertEquals(TEXT, applet.getTextFileContent(DATA_FILE_URI));

		Assert.assertEquals(TEXT, applet.getTextFromBase64(DATA_BASE64));

		Assert.assertEquals(DATA_BASE64, applet.getBase64FromText(TEXT));

	}

}
