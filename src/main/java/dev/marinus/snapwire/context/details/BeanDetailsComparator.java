package dev.marinus.snapwire.context.details;


import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class BeanDetailsComparator implements Comparator<BeanDetails> {


    @Override
    public int compare(BeanDetails o1, BeanDetails o2) {
        // check if bean depends deep on other bean
        if (o1.getDependencies().contains(o2)) {
            return 1;
        }
        if (o2.getDependencies().contains(o1)) {
            return -1;
        }
        if (getDependencies(o1).contains(o2)) {
            return 1;
        }
        if (getDependencies(o2).contains(o1)) {
            return -1;
        }
        return 0;
    }

    public Set<BeanDetails> getDependencies(BeanDetails beanDetails) {
        Set<BeanDetails> dependencies = new HashSet<>();
        dependencies.addAll(beanDetails.getDependencies());
        for (BeanDetails dependency : beanDetails.getDependencies()) {
            dependencies.addAll(getDependencies(dependency));
        }
        return dependencies;
    }
}
