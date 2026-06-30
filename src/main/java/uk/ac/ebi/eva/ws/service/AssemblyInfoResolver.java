package uk.ac.ebi.eva.ws.service;

import org.springframework.stereotype.Service;
import uk.ac.ebi.eva.ws.repository.jpa.AssemblyInfoProjection;
import uk.ac.ebi.eva.ws.repository.jpa.AssemblyRepository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class AssemblyInfoResolver {

    private final AssemblyRepository assemblyRepository;
    private final ConcurrentMap<String, Optional<AssemblyInfoProjection>> assemblyInfoCache = new ConcurrentHashMap<>();

    public AssemblyInfoResolver(AssemblyRepository assemblyRepository) {
        this.assemblyRepository = assemblyRepository;
    }

    public Optional<AssemblyInfoProjection> resolve(Integer taxonomyAccession, String assemblyAccession) {
        return assemblyInfoCache.computeIfAbsent(assemblyAccession, key -> assemblyRepository.findAssemblyInfo(taxonomyAccession, key));
    }
}