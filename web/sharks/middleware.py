
class CustomMiddleWares(object):
    def __init__(self, get_response):
        self.get_response = get_response

    def __call__(self, request):
        request.message = request.session.pop("message", "")
        request.error_message = request.session.pop("error_message", "")
        return self.get_response(request)
