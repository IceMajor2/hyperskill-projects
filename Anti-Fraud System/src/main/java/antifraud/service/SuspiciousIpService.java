package antifraud.service;

import antifraud.model.SuspiciousIp;
import antifraud.repository.SuspiciousIpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class SuspiciousIpService {

    private static final String IP_REGEX = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";

    @Autowired
    private SuspiciousIpRepository suspiciousIpRepository;

    public SuspiciousIp saveSuspiciousIp(SuspiciousIp ip) {
        if (suspiciousIpRepository.findByIp(ip.getIp()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (!isIpValid(ip.getIp())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        suspiciousIpRepository.save(ip);
        return ip;
    }

    public SuspiciousIp deleteSuspiciousIp(String ip) {
        if (!isIpValid(ip)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Optional<SuspiciousIp> optSusIp = suspiciousIpRepository.findByIp(ip);
        if (optSusIp.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        SuspiciousIp susIp = optSusIp.get();
        suspiciousIpRepository.delete(susIp);
        return susIp;
    }

    public List<SuspiciousIp> listOfSuspiciousIps() {
        return suspiciousIpRepository.findAllByOrderByIdAsc();
    }

    public static boolean isIpValid(String ip) {
        return ip.matches(IP_REGEX);
    }
}
