package es.gob.afirma.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import es.gob.afirma.core.misc.AOUtil;
import es.gob.afirma.signers.padestri.server.DocumentManager;

/** Fachada simulada de gestor documental.
 * @author Tom&aacute;s Garc&iacute;a-;er&aacute;s */
final class FakeDocumentManager implements DocumentManager {

	private static final String PDF_DOC = "TEST_PDF_SIN_FIRMA.pdf"; //$NON-NLS-1$
	//private static final String SIGNED_PDF_DOC = "TEST_PDF.pdf"; //$NON-NLS-1$

	/** {@inheritDoc} */
	@Override
	public byte[] getDocument(final String id) throws IOException {
		return AOUtil.getDataFromInputStream(this.getClass().getResourceAsStream(PDF_DOC));
	}

	/** {@inheritDoc} */
	@Override
	public String storeDocument(final String id, final byte[] data) throws IOException {
		final File tempFile = File.createTempFile("fakeDocumentRetriever-" + id, ".pdf"); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			final FileOutputStream fos = new FileOutputStream(tempFile);
			fos.write(data);
			fos.close();
		} catch (final Exception e) {
			System.err.println("Error al generar la firma temporal: " + tempFile.getAbsolutePath()); //$NON-NLS-1$
		}
		System.out.println("Guardamos la firma generada en: " + tempFile.getAbsolutePath()); //$NON-NLS-1$
		return "id-fake-" + id; //$NON-NLS-1$
	}
}