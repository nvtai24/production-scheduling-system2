package org.nvtai.ProductionSchedulingSystem.service;

import org.nvtai.ProductionSchedulingSystem.entity.Feature;
import org.nvtai.ProductionSchedulingSystem.repository.FeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class FeatureService {

    @Autowired
    private FeatureRepository featureRepository;

    // Phương thức để lấy danh sách quyền từ database
    public LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> loadRequestMapFromDatabase() {
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<>();

        List<Feature> features = featureRepository.findAll(); // Giả sử phương thức này trả về danh sách quyền từ database
        for (Feature feature : features) {
            String url = feature.getUrl();
            String method = feature.getMethod();

            ConfigAttribute attribute = (ConfigAttribute) () -> url + ":" + method; // Tạo quyền dưới dạng URL:METHOD
            requestMap.put(new AntPathRequestMatcher(url, method), Collections.singletonList(attribute));
        }

        return requestMap;
    }
}