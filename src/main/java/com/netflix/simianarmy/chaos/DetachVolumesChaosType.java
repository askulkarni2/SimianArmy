package com.netflix.simianarmy.chaos;

import java.util.List;

import com.netflix.simianarmy.CloudClient;

/**
 * We force-detach all the EBS volumes.
 * 
 * This is supposed to simulate a catastrophic failure of EBS, however the instance
 * will (possibly) still keep running; e.g. it should continue to respond to pings.
 *
 */
public class DetachVolumesChaosType extends ChaosType {
    public static final DetachVolumesChaosType INSTANCE = new DetachVolumesChaosType();

    protected DetachVolumesChaosType() {
        super("DetachVolumes");
    }

    @Override
    public boolean canApply(CloudClient cloudClient, String instanceId) {
        List<String> volumes = cloudClient.listAttachedVolumes(instanceId);
        return !volumes.isEmpty();
    }

    @Override
    public void apply(CloudClient cloudClient, String instanceId) {
        // IDEA: We could have a strategy where we detach some of the volumes...
        boolean force = true;
        for (String volumeId : cloudClient.listAttachedVolumes(instanceId)) {
            cloudClient.detachVolume(instanceId, volumeId, force);
        }
    }

}
