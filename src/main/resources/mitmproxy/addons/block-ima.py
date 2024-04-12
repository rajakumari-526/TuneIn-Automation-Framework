"""
Addon which blocks IMA ads

Usage:

    mitmproxy -s block-ima.py

    and then send a HTTP request to trigger the shutdown:
    curl --proxy localhost:8089 http://blockima.com/path
    curl --proxy localhost:8089 http://unblockima.com/path
"""
import logging

from mitmproxy import ctx
from mitmproxy import http

def load(l):
    logging.info("Load script block-ima")

def request(flow: http.HTTPFlow) -> None:
    if flow.request.pretty_url == "http://blockima.com/path":
        logging.info("Block IMA ads")
        ctx.options.block_list = [":~d doubleclick|adsrvr:418"]
        flow.kill()
    if flow.request.pretty_url == "http://unblockima.com/path":
        logging.info("Unblock IMA ads")
        ctx.options.block_list = []
        flow.kill()
