package uni.project.proxmox.model;

/**
 * The {@code ProxmoxCredentials} class holds the necessary authentication details
 * required to connect and interact with the Proxmox API using an API token.
 *
 * <p>It encapsulates the Token ID and Secret, ensuring that neither value
 * is null or empty upon initialization or modification.</p>
 */
public class ProxmoxCredentials{

        private String tokenId;
        private String secret;

        /**
         * Constructs a new {@code ProxmoxCredentials} instance with the provided token ID and secret.
         *
         * @param tokenId the unique identifier for the Proxmox API token
         * @param secret  the secret key associated with the Proxmox API token
         * @throws IllegalArgumentException if either {@code tokenId} or {@code secret} is null or empty
         */
        public ProxmoxCredentials(String tokenId, String secret){
                setSecret(secret);
                setTokenId(tokenId);
        }


        /**
         * Sets or updates the secret key for the Proxmox API token.
         *
         * @param secret the secret key to set
         * @throws IllegalStateException if the provided {@code secret} is null or empty
         */
        public void setSecret(String secret) {
                if (secret == null || secret.isEmpty()) {
                        throw new IllegalArgumentException("The secret for the API token is not inserted!");
                }
                this.secret = secret;
        }

        /**
         * Sets or updates the token identification for the Proxmox API token.
         *
         * @param tokenId the token identification to set
         * @throws IllegalStateException if the provided {@code tokenId} is null or empty
         */
        public void setTokenId(String tokenId) {
                if(tokenId == null || tokenId.isEmpty()) {
                        throw new IllegalArgumentException("The token identification is not inserted!");
                }

                this.tokenId = tokenId;
        }

        /**
         * Retrieves the secret key associated with the Proxmox API token.
         *
         * @return the API token secret
         */
        public String getSecret() {
                return this.secret;
        }

        /**
         * Retrieves the token identification associated with the Proxmox API token.
         *
         * @return the token ID
         */
        public String getTokenId() {
                return this.tokenId;
        }
}
