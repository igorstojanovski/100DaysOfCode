package co.igorski.mongodbspringexample.controllers;

import co.igorski.mongodbspringexample.model.Organization;
import co.igorski.mongodbspringexample.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization organization) {
        Organization created = organizationService.createOrganization(organization);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

}
