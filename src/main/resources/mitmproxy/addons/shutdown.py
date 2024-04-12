"""
A simple way of shutting down the mitmproxy instance to stop everything.

Usage:

    mitmproxy -s shutdown.py

    and then send a HTTP request to trigger the shutdown:
    curl --proxy localhost:8089 http://shutdown.com/path
"""
import logging

from mitmproxy import ctx
from mitmproxy import http

def load(l):
    logging.info("Load script shutdown")

def request(flow: http.HTTPFlow) -> None:
    # a random condition to make this example a bit more interactive
    if flow.request.pretty_url == "http://shutdown.com/path":
        logging.info("Shutting down everything...")
        flow.kill()
        ctx.master.shutdown()