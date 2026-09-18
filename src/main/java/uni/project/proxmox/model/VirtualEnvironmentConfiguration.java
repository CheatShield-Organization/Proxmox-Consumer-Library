package uni.project.proxmox.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code VirtualEnvironmentConfiguration} class is
 * used for configuring request bodies across various Proxmox VE API paths (such as VM creation and cloning).
 *
 * @apiNote To learn more about specific configuration properties and parameters, consult the
 *          <a href="https://pve.proxmox.com/pve-docs/api-viewer/">Proxmox API Viewer</a>.
 */
public class VirtualEnvironmentConfiguration {

    private final Map<String,Object> VeProperties = new HashMap<>();;

    /**
     * Adds a Virtual Environment property dynamically.
     * Handled by Jackson during JSON deserialization via {@link JsonAnySetter}.
     *
     * @param propertyName the name of the configuration property
     * @param value        the value associated with the property
     */
    @JsonAnySetter
    public void addVeProperty(String propertyName, Object value) {
        VeProperties.put(propertyName,value);
    }

    /**
     * Retrieves all configured Virtual Environment properties as a map.
     * Handled by Jackson during JSON serialization via {@link JsonAnyGetter}.
     *
     * @return a {@link Map} containing all dynamic properties and their respective values
     */
    @JsonAnyGetter
    public Map<String, Object> getVeProperties() {
        return VeProperties;
    }



}
