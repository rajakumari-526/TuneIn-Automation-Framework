import logging

from mitmproxy import http

def request(flow: http.HTTPFlow) -> None:
    if "/1.0/mediate?" in flow.request.pretty_url:
        logging.info("Modify query")
        flow.request.query["KoTJ4pWxzq"] = "1"
