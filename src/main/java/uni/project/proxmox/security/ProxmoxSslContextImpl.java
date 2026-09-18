package uni.project.proxmox.security;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

/**
 * The {@code ProxmoxSslContextImpl} is used to establish a SSL layer between the client and the Proxmox server.
 * @apiNote This class should be run only one time and should reuse the KeyStore inside the memory until the certification is
 * over.
 */
public class ProxmoxSslContextImpl {

    private Certificate proxmoxCertificate;

    private KeyStore proxmoxKeyStoreCache;

    private TrustManagerFactory proxmoxTrustManagerFactory;

    private SSLContext cachedSslContext;
/**
 *
 *
 *
 */
    public ProxmoxSslContextImpl(File certificationFile) {
        try {
            extractCertificationKey(certificationFile);
            storeCertificationKey();
            extractSslContext();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Proxmox SSL Configuration \n Message: "+ e.getMessage());
        }
    }
/**
 *
 *
 */
    private void extractCertificationKey(File certificationFile) throws Exception {
        String certificateType = "X.509";

        CertificateFactory factory = CertificateFactory.getInstance(certificateType);

        try (InputStream is = new FileInputStream(certificationFile)) {
            this.proxmoxCertificate = factory.generateCertificate(is);
        }
    }
/**
 *
 *
 *
 *
 */
    private void storeCertificationKey() throws Exception {

        this.proxmoxKeyStoreCache = KeyStore.getInstance(KeyStore.getDefaultType());
        this.proxmoxKeyStoreCache.load(null, null);
        this.proxmoxKeyStoreCache.setCertificateEntry("proxmox-server", this.proxmoxCertificate);


        this.proxmoxTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        this.proxmoxTrustManagerFactory.init(this.proxmoxKeyStoreCache);

    }
/**
 *
 *
 *
 *
 */
    private SSLContext extractSslContext() throws NoSuchAlgorithmException, KeyManagementException {

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, this.proxmoxTrustManagerFactory.getTrustManagers(), null);
        this.cachedSslContext = sslContext;

        return this.cachedSslContext;

    }
/**
 *
 *
 *
 */
    public SSLContext getSslContext() {
        return cachedSslContext;
    }

}