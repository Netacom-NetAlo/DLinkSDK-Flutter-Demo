import 'package:flutter/material.dart';
import 'package:flutter_sdk/model/neta_user.dart';
import 'package:flutter_sdk/options_sdk_page.dart';
import 'package:flutter_sdk/platform_channel.dart';

class ConnectSdkPage extends StatelessWidget {
  ConnectSdkPage({super.key});
  final platformChannel = PlatformChannel();
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Kết nối SDK"),
        elevation: 0,
      ),
      body: Center(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisSize: MainAxisSize.min,
          children: [
            // Set User A
            const SizedBox(
              height: 50,
            ),
            TextButton(
              onPressed: () async {
                await platformChannel.setSdkUser(
                  user: NetaUser(
                      id: 4785074606697392,
                      token: "09943cee28b0ac2c4cac9280b3fcbddbe199vzGF",
                      username: "son1990",
                      avatar: ""),
                );
                // ignore: use_build_context_synchronously
                Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) => OptionsSdkPage(
                            platformChannel: platformChannel,
                          )),
                );
              },
              style: ButtonStyle(
                  backgroundColor: MaterialStateProperty.all(Colors.orange)),
              child: const SizedBox(
                width: 150,
                height: 40,
                child: Center(
                    child: Text(
                  "Set User A",
                  style: TextStyle(color: Colors.white, fontSize: 16),
                )),
              ),
            ),
            // Set User B
            const SizedBox(
              height: 50,
            ),
            TextButton(
              onPressed: () async {
                await platformChannel.setSdkUser(
                  user: NetaUser(
                      id: 4785074606697392,
                      token: "09943cee28b0ac2c4cac9280b3fcbddbe199vzGF",
                      username: "son1990",
                      avatar: ""),
                );
                // ignore: use_build_context_synchronously
                Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) => OptionsSdkPage(
                            platformChannel: platformChannel,
                          )),
                );
              },
              style: ButtonStyle(
                  backgroundColor: MaterialStateProperty.all(Colors.orange)),
              child: const SizedBox(
                width: 150,
                height: 40,
                child: Center(
                    child: Text(
                  "Set User B",
                  style: TextStyle(color: Colors.white, fontSize: 16),
                )),
              ),
            )
          ],
        ),
      ),
    );
  }
}
