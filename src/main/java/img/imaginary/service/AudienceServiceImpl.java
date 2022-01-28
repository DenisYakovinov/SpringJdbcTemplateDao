package img.imaginary.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import img.imaginary.dao.AudienceDao;
import img.imaginary.exception.ServiceException;
import img.imaginary.service.entity.Audience;

@Service
public class AudienceServiceImpl implements AudienceService {

    private AudienceDao audienceDao;
     
    @Autowired
    public AudienceServiceImpl(AudienceDao audienceDao) {
        this.audienceDao = audienceDao;
    }

    @Override
    public Audience add(Audience audience) {
        int audienceId = audienceDao.add(audience).orElseThrow(() -> new ServiceException("audience wasn't added"));
        audience.setAudienceId(audienceId);
        return audience;
    }

    @Override
    public List<Audience> findAll() {
        return audienceDao.findAll();
    }

    @Override
    public Audience findById(int id) {
        return audienceDao.findById(id);
    }

    @Override
    public void update(Audience audience) {
        audienceDao.update(audience);        
    }

    @Override
    public void delete(int id) {
        audienceDao.delete(id);
    }

    @Override
    public void delete(Audience audience) {
        audienceDao.delete(audience);       
    }
}
