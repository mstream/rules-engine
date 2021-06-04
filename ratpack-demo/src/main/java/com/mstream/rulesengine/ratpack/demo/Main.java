package com.mstream.rulesengine.ratpack.demo;

import ch.qos.logback.classic.Level;
import com.mstream.rulesengine.core.ValidationOptions;
import com.mstream.rulesengine.ratpack.RatpackRulesEngine;
import com.mstream.rulesengine.ratpack.RulesMatcher;
import com.mstream.rulesengine.ratpack.config.ConfigValidator;
import com.mstream.rulesengine.ratpack.demo.actionhandler.*;
import com.mstream.rulesengine.ratpack.demo.actionprehandler.StreamingTicketIdGenerationActionPreHandlerCreator;
import com.mstream.rulesengine.ratpack.demo.admin.*;
import com.mstream.rulesengine.ratpack.demo.content.ContentService;
import com.mstream.rulesengine.ratpack.demo.domain.DomainDao;
import com.mstream.rulesengine.ratpack.demo.factprovider.DoesUserHaveFreeStreamingSlotsProviderCreator;
import com.mstream.rulesengine.ratpack.demo.factprovider.IsContentFreeToAirFactProviderCreator;
import com.mstream.rulesengine.ratpack.demo.factprovider.IsHouseholdBlockedFactProviderCreator;
import com.mstream.rulesengine.ratpack.demo.factprovider.IsTerritoryBlockedFactProviderCreator;
import com.mstream.rulesengine.ratpack.demo.household.HouseholdDao;
import com.mstream.rulesengine.ratpack.demo.streamingticket.StreamingTicketsDao;
import org.slf4j.LoggerFactory;
import ratpack.server.RatpackServer;


public class Main {

    public static void main(String[] args) throws Exception {

        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME))
                .setLevel(Level.INFO);

        RatpackServer.start(server ->
                server.registryOf(registry ->
                        registry.add(new RatpackRulesEngine())
                                .add(new ConfigValidator())
                                .add(new ValidationOptions(
                                        ValidationOptions.Exclusions.DUPLICATED_ACTIONS,
                                        ValidationOptions.Exclusions.DUPLICATED_CONDITIONS))
                                .add(new RulesMatcher())
                                .add(new GetRulesHandler())
                                .add(new RulesStringKeywordExpander())
                                .add(new RulesStringKeywordShortener())
                                .add(new GetRulesHandler())
                                .add(new GetActionsHandler())
                                .add(new GetFactsHandler())
                                .add(new UpdateRulesHandler())
                                .add(new ValidateRulesHandler())
                                .add(new StartStreamHandler())
                                .add(new ProfileHandler())
                                .add(new RulesSupplier())
                                .add(new IsHouseholdBlockedFactProviderCreator())
                                .add(new DoesUserHaveFreeStreamingSlotsProviderCreator())
                                .add(new IsTerritoryBlockedFactProviderCreator())
                                .add(new IsContentFreeToAirFactProviderCreator())
                                .add(new StreamingTicketIdGenerationActionPreHandlerCreator())
                                .add(new CreateStreamingTicketActionHandlerCreator())
                                .add(new ReturnStreamingForbiddenResponseActionHandlerCreator())
                                .add(new ReturnStreamingAllowedResponseActionHandlerCreator())
                                .add(new SendFailureAuditEventActionHandlerCreator())
                                .add(new SendAuditEventActionHandlerCreator())
                                .add(new ContentService())
                                .add(new DomainDao())
                                .add(new HouseholdDao())
                                .add(new StreamingTicketsDao()))
                        .handlers(chain ->
                                chain.prefix("stream", streamChain ->
                                        streamChain
                                                .all(ProfileHandler.class)
                                                .get(StartStreamHandler.class))
                                        .path("admin/actions", GetActionsHandler.class)
                                        .path("admin/facts", GetFactsHandler.class)
                                        .path("admin/rules/validate", ValidateRulesHandler.class)
                                        .path("admin/rules", ctx ->
                                                ctx.byMethod(method ->
                                                        method
                                                                .get(GetRulesHandler.class)
                                                                .put(UpdateRulesHandler.class)))));
    }
}
