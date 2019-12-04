package software.amazon.ecr.repository;

import software.amazon.cloudformation.proxy.AmazonWebServicesClientProxy;
import software.amazon.cloudformation.proxy.Logger;
import software.amazon.cloudformation.proxy.ProgressEvent;
import software.amazon.cloudformation.proxy.OperationStatus;
import software.amazon.cloudformation.proxy.ResourceHandlerRequest;
import software.amazon.cloudformation.exceptions.ResourceNotFoundException;
import software.amazon.awssdk.services.ecr.model.RepositoryNotFoundException;

public class DeleteHandler extends BaseHandler<CallbackContext> {

    @Override
    public ProgressEvent<ResourceModel, CallbackContext> handleRequest(
            final AmazonWebServicesClientProxy proxy,
            final ResourceHandlerRequest<ResourceModel> request,
            final CallbackContext callbackContext,
            final Logger logger) {

        final ResourceModel model = request.getDesiredResourceState();
        final String repositoryName = model.getRepositoryName();

        try {
            proxy.injectCredentialsAndInvokeV2(Translator.deleteRepositoryRequest(model), ClientBuilder.getClient()::deleteRepository);
            logger.log(String.format("%s [%s] Deleted Successfully", ResourceModel.TYPE_NAME, repositoryName));
        } catch (RepositoryNotFoundException e) {
            throw new ResourceNotFoundException(ResourceModel.TYPE_NAME, repositoryName);
        }

        return ProgressEvent.<ResourceModel, CallbackContext>builder()
                .resourceModel(model)
                .status(OperationStatus.SUCCESS)
                .build();
    }
}