package twino.test.loan.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twino.test.loan.dao.BlacklistDao;

import javax.transaction.Transactional;

@Component
@Transactional
public class BlacklistService {
    @Autowired
    private BlacklistDao blacklistDao;

    public boolean isBlacklisted(long personId) {
        return blacklistDao.exist(personId);
    }

    public void blacklistPerson(long personId) {
        if(!isBlacklisted(personId)) {
            blacklistDao.insert(personId);
        }
    }
}
