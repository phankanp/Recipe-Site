package phan.recipesite.service;

import org.springframework.stereotype.Service;
import phan.recipesite.model.Role;
import phan.recipesite.repository.RoleRepository;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}
