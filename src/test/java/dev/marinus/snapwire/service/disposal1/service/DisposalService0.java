package dev.marinus.snapwire.service.disposal1.service;

import dev.marinus.snapwire.DisposableBean;
import dev.marinus.snapwire.annotation.Service;
import lombok.Getter;

@Service
@Getter
public class DisposalService0 implements DisposableBean {

    private Long id = 1L;

    @Override
    public void destroy() throws Exception {
        id = null;
    }
}
