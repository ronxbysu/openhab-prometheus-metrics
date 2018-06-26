package org.openhab.binding.openhabprometheusmetrics.internal;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingRegistry;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.internal.ThingManager;
import org.eclipse.smarthome.core.thing.internal.ThingRegistryImpl;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true)
public class OpenHABPrometheusMetricsThingManager extends ThingManager {

    private Map<ThingUID, Thing> thingsPool = new ConcurrentHashMap<>();

    private ThingRegistryImpl thingRegistry;

    @Override
    public void thingAdded(Thing thing, ThingTrackerEvent thingTrackerEvent) {
        super.thingAdded(thing, thingTrackerEvent);
        thingsPool.put(thing.getUID(), thing);
    }

    @Override
    public void thingRemoving(Thing thing, ThingTrackerEvent thingTrackerEvent) {
        super.thingRemoving(thing, thingTrackerEvent);
        thingsPool.put(thing.getUID(), thing);
    }

    @Override
    public void thingRemoved(Thing thing, ThingTrackerEvent thingTrackerEvent) {
        thingsPool.remove(thing.getUID());

    }

    @Override
    public void thingUpdated(Thing thing, ThingTrackerEvent thingTrackerEvent) {
        thingsPool.put(thing.getUID(), thing);
    }

    @Override
    @Activate
    protected synchronized void activate(ComponentContext componentContext) {
        super.activate(componentContext);
        thingRegistry.addThingTracker(this);
    }

    @Override
    @Reference
    protected void setThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = (ThingRegistryImpl) thingRegistry;
    }

    @Override
    protected void unsetThingRegistry(ThingRegistry thingRegistry) {
        this.thingRegistry = null;
    }

    public Set<Thing> getThingSet() {
        return new HashSet<>(thingsPool.values());
    }

}