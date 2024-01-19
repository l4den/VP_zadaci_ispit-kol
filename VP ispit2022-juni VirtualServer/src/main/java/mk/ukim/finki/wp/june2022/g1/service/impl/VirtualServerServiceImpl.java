package mk.ukim.finki.wp.june2022.g1.service.impl;

import mk.ukim.finki.wp.june2022.g1.model.OSType;
import mk.ukim.finki.wp.june2022.g1.model.User;
import mk.ukim.finki.wp.june2022.g1.model.VirtualServer;
import mk.ukim.finki.wp.june2022.g1.model.exceptions.InvalidUserIdException;
import mk.ukim.finki.wp.june2022.g1.model.exceptions.InvalidVirtualMachineIdException;
import mk.ukim.finki.wp.june2022.g1.repository.UserRepository;
import mk.ukim.finki.wp.june2022.g1.repository.VirtualServerRepository;
import mk.ukim.finki.wp.june2022.g1.service.VirtualServerService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VirtualServerServiceImpl implements VirtualServerService {

    private final VirtualServerRepository virtualServerRepository;
    private final UserRepository userRepository;

    public VirtualServerServiceImpl(VirtualServerRepository virtualServerRepository, UserRepository userRepository) {
        this.virtualServerRepository = virtualServerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<VirtualServer> listAll() {
        return virtualServerRepository.findAll();
    }

    @Override
    public VirtualServer findById(Long id) {
        return virtualServerRepository.findById(id).orElseThrow(()->new InvalidVirtualMachineIdException(id));
    }

    @Override
    public VirtualServer create(String name, String ipAddress, OSType osType, List<Long> owners, LocalDate launchDate) {
        List<User> owners_objs = userRepository.findAllById(owners);
        return virtualServerRepository.save(new VirtualServer(name, ipAddress, osType, owners_objs, launchDate));
    }

    @Override
    public VirtualServer update(Long id, String name, String ipAddress, OSType osType, List<Long> owners) {
        VirtualServer virtualServer = virtualServerRepository.findById(id).orElseThrow(()->new InvalidVirtualMachineIdException(id));
        List<User> owners_objs = userRepository.findAllById(owners);
        virtualServer.setInstanceName(name);
        virtualServer.setIpAddress(ipAddress);
        virtualServer.setOSType(osType);
        virtualServer.setOwners(owners_objs);
        return virtualServerRepository.save(virtualServer);
    }

    @Override
    public VirtualServer delete(Long id) {
        VirtualServer virtualServer = virtualServerRepository.findById(id).orElseThrow(()->new InvalidVirtualMachineIdException(id));
        virtualServerRepository.delete(virtualServer);
        return virtualServer;
    }

    @Override
    public VirtualServer markTerminated(Long id) {
        VirtualServer virtualServer = virtualServerRepository.findById(id).orElseThrow(()->new InvalidVirtualMachineIdException(id));
        virtualServer.setTerminated(true);
        return virtualServerRepository.save(virtualServer);
    }

    @Override
    public List<VirtualServer> filter(Long ownerId, Integer activeMoreThanDays) {
        if(ownerId == null && activeMoreThanDays == null){
            return virtualServerRepository.findAll();
        }
        if(ownerId == null){
            LocalDate before_date = LocalDate.now().minusDays(activeMoreThanDays);
            return virtualServerRepository.findAllByLaunchDateBefore(before_date);
        }
        User owner = userRepository.findById(ownerId).orElseThrow(()->new InvalidUserIdException(ownerId));
        if(activeMoreThanDays == null){
            return virtualServerRepository.findAllByOwnersContains(owner);
        }
        LocalDate before_date = LocalDate.now().minusDays(activeMoreThanDays);
        return virtualServerRepository.findAllByOwnersContainsAndLaunchDateBefore(owner, before_date);
    }
}
