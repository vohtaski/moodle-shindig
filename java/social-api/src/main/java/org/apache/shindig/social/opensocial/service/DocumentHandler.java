/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.shindig.social.opensocial.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.util.FutureUtil;
import org.apache.shindig.config.ContainerConfig;
import org.apache.shindig.protocol.HandlerPreconditions;
import org.apache.shindig.protocol.Operation;
import org.apache.shindig.protocol.ProtocolException;
import org.apache.shindig.protocol.RequestItem;
import org.apache.shindig.protocol.RestfulCollection;
import org.apache.shindig.protocol.Service;
import org.apache.shindig.social.opensocial.model.Document;
import org.apache.shindig.social.opensocial.spi.CollectionOptions;
import org.apache.shindig.social.opensocial.spi.GroupId;
import org.apache.shindig.social.opensocial.spi.DocumentId;
import org.apache.shindig.social.opensocial.spi.DocumentService;
import org.apache.shindig.social.opensocial.spi.UserId;
import org.apache.shindig.social.opensocial.spi.Context;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;

/**
 * RPC/REST handler for all /documents requests
 */
@Service(name = "documents", path = "/{contextId}+/{contextType}")
public class DocumentHandler {
  private final DocumentService documentService;
  private final ContainerConfig config;

  @Inject
  public DocumentHandler(DocumentService documentService, ContainerConfig config) {
    this.documentService = documentService;
    this.config = config;
  }

  /**
   * Allowed end-points /documents/{contextId}/{contextType} /documents/{documentId}+ 
   *
   * examples: /documents/john.doe/@person /documents/tex.group/@space /documents/mywidget
   */
  @Operation(httpMethods = "GET")
  public Future<?> get(SocialRequestItem request) throws ProtocolException {
    Set<String> fields = request.getFields(Document.Field.DEFAULT_FIELDS);
    Set<String> contextIds = request.getContextIds();
    String contextType = request.getContextType();

    // Preconditions
    HandlerPreconditions.requireNotEmpty(contextIds, "No contextId is specified");
    
    CollectionOptions options = new CollectionOptions(request);
    if(contextType == null){
    	// when contextType is not specified, get list of documents specified by ids
    	if(contextIds.size() == 1){
    		return documentService.getDocument(new DocumentId(contextIds.iterator().next()), fields, request.getToken());
    	}else{
    	    ImmutableSet.Builder<DocumentId> ids = ImmutableSet.builder();
    	    for (String id : contextIds) {
    	    	ids.add(new DocumentId(id));
    	    }
    	    Set<DocumentId> documentIds = ids.build();
    		
    		return documentService.getDocuments(documentIds, options, fields, request.getToken());
    	}
    }else{
    	// contextType is specified, get a list of documents for this context
    	if(contextIds.size() == 1){
    		Context context = new Context(contextIds.iterator().next(),contextType);
    		return documentService.getDocumentsForContext(context, options, fields, request.getToken());
    	}else{
    		throw new IllegalArgumentException("Cannot fetch documents for multiple contexts");
    	}
    }
    
  }

  @Operation(httpMethods = "GET", path="/@supportedFields")
  public List<Object> supportedFields(RequestItem request) {
    // TODO: Would be nice if name in config matched name of service.
    String container = Objects.firstNonNull(request.getToken().getContainer(), "default");
    return config.getList(container,
        "${Cur['gadgets.features'].opensocial.supportedFields.document}");
  }
}
