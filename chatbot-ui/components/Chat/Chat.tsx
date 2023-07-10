import { IconClearAll, IconSettings } from '@tabler/icons-react';
import {
  MutableRefObject,
  memo,
  useCallback,
  useContext,
  useEffect,
  useRef,
  useState,
} from 'react';
import toast from 'react-hot-toast';

import { useTranslation } from 'next-i18next';

import { getEndpoint } from '@/utils/app/api';
import {
  saveConversation,
  saveConversations,
  updateConversation,
} from '@/utils/app/conversation';
import { throttle } from '@/utils/data/throttle';

import { ChatBody, Conversation, Message } from '@/types/chat';
import { Plugin } from '@/types/plugin';

import HomeContext from '@/pages/api/home/home.context';

import Spinner from '../Spinner';
import { ChatInput } from './ChatInput';
import { ChatLoader } from './ChatLoader';
import { ErrorMessageDiv } from './ErrorMessageDiv';
import { MemoizedChatMessage } from './MemoizedChatMessage';
import { ModelSelect } from './ModelSelect';
import { RegisterDialog } from './RegisterDialog';
import { SystemPrompt } from './SystemPrompt';
import { TemperatureSlider } from './Temperature';

import axios from 'axios';
import { randomBytes } from 'crypto';
import crypto from 'crypto-js';

interface Props {
  stopConversationRef: MutableRefObject<boolean>;
}

export const Chat = memo(({ stopConversationRef }: Props) => {
  const { t } = useTranslation('chat');

  const {
    state: {
      selectedConversation,
      conversations,
      models,
      apiKey,
      pluginKeys,
      serverSideApiKeyIsSet,
      messageIsStreaming,
      modelError,
      loading,
      prompts,
      loggedIn,
    },
    handleUpdateConversation,
    dispatch: homeDispatch,
  } = useContext(HomeContext);

  const [currentMessage, setCurrentMessage] = useState<Message>();
  const [autoScrollEnabled, setAutoScrollEnabled] = useState<boolean>(true);
  const [showSettings, setShowSettings] = useState<boolean>(false);
  const [showScrollDownButton, setShowScrollDownButton] =
    useState<boolean>(false);

  const messagesEndRef = useRef<HTMLDivElement>(null);
  const chatContainerRef = useRef<HTMLDivElement>(null);
  const textareaRef = useRef<HTMLTextAreaElement>(null);

  const sendMessage = async (receiver: string, message: string) => {
    if (selectedConversation) {
      const socket = new WebSocket('ws://localhost:8000/chat')
      socket.onopen = () => {
        console.log('Kết nối WebSocket đã được thiết lập.');
      };
      
      try {
        console.log("wtf")
        const response = await axios.get(
          `/public-key/${selectedConversation.name}`,
        );

        const publicKey = response.data;

        const secretKey = randomBytes(64).toString('hex');

        console.log(publicKey);
        console.log(secretKey);
        const encryptedMessage = crypto.AES.encrypt(message, secretKey);
      } catch (err) {
        return;
      }
    }
  };

  const handleSend = useCallback(
    async (message: Message, deleteCount = 0, plugin: Plugin | null = null) => {
      if (selectedConversation) {
        let updatedConversation: Conversation;
        if (deleteCount) {
          const updatedMessages = [...selectedConversation.messages];
          for (let i = 0; i < deleteCount; i++) {
            updatedMessages.pop();
          }
          updatedConversation = {
            ...selectedConversation,
            messages: [...updatedMessages, message],
          };
        } else {
          updatedConversation = {
            ...selectedConversation,
            messages: [...selectedConversation.messages, message],
          };
        }
        homeDispatch({
          field: 'selectedConversation',
          value: updatedConversation,
        });
        // homeDispatch({ field: 'loading', value: true });
        // homeDispatch({ field: 'messageIsStreaming', value: true });
        const chatBody: ChatBody = {
          model: updatedConversation.model,
          messages: updatedConversation.messages,
          key: apiKey,
          prompt: updatedConversation.prompt,
          temperature: updatedConversation.temperature,
        };
        const endpoint = getEndpoint(plugin);
        let body;
        if (!plugin) {
          body = JSON.stringify(chatBody);
        } else {
          body = JSON.stringify({
            ...chatBody,
            googleAPIKey: pluginKeys
              .find((key) => key.pluginId === 'google-search')
              ?.requiredKeys.find((key) => key.key === 'GOOGLE_API_KEY')?.value,
            googleCSEId: pluginKeys
              .find((key) => key.pluginId === 'google-search')
              ?.requiredKeys.find((key) => key.key === 'GOOGLE_CSE_ID')?.value,
          });
        }
        const controller = new AbortController();
        const response = {
          ok: true,
          body: 'Some very random text',
          statusText: '',
        };
        if (!response.ok) {
          // homeDispatch({ field: 'loading', value: false });
          // homeDispatch({ field: 'messageIsStreaming', value: false });
          toast.error(response.statusText);
          return;
        }
        const data = response.body;
        if (!data) {
          // homeDispatch({ field: 'loading', value: false });
          // homeDispatch({ field: 'messageIsStreaming', value: false });
          return;
        }
        // if (!plugin) {
        // Upon connecting to another user to send message or something
        if (updatedConversation.messages.length === 1) {
          const { content } = message;
          const customName =
            content.length > 30 ? content.substring(0, 30) + '...' : content;

          updatedConversation = {
            ...updatedConversation,
            name: customName,
          };

          try {
            console.log("wtf 123")
            const socket = new WebSocket('ws://localhost:8000/chat')
            socket.onopen = () => {
              console.log('Kết nối WebSocket đã được thiết lập.');
            };
            const response = await axios.get(
              `/public-key/${customName}`,
            );
          } catch (err) {
            console.log(err);
            return;
          }
        } else {
          sendMessage(selectedConversation.name, message.content);
        }

        // something should be handled upon receiving messages, don't know how yet.
        // if (Math.floor(Math.random() * 3) == 1) {
        //   let text = '';
        //   const value = data;
        //   const chunkValue = value;
        //   text += chunkValue;

        //   const updatedMessages: Message[] = [
        //     ...updatedConversation.messages,
        //     { role: 'assistant', content: chunkValue },
        //   ];
        //   updatedConversation = {
        //     ...updatedConversation,
        //     messages: updatedMessages,
        //   };
        // }

        homeDispatch({
          field: 'selectedConversation',
          value: updatedConversation,
        });

        saveConversation(updatedConversation);
        const updatedConversations: Conversation[] = conversations.map(
          (conversation) => {
            if (conversation.id === selectedConversation.id) {
              return updatedConversation;
            }
            return conversation;
          },
        );
        if (updatedConversations.length === 0) {
          updatedConversations.push(updatedConversation);
        }
        homeDispatch({ field: 'conversations', value: updatedConversations });
        saveConversations(updatedConversations);
        // homeDispatch({ field: 'messageIsStreaming', value: false });
      }
    },
    [
      apiKey,
      conversations,
      pluginKeys,
      selectedConversation,
      stopConversationRef,
    ],
  );

  const scrollToBottom = useCallback(() => {
    if (autoScrollEnabled) {
      messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
      textareaRef.current?.focus();
    }
  }, [autoScrollEnabled]);

  const handleScroll = () => {
    if (chatContainerRef.current) {
      const { scrollTop, scrollHeight, clientHeight } =
        chatContainerRef.current;
      const bottomTolerance = 30;

      if (scrollTop + clientHeight < scrollHeight - bottomTolerance) {
        setAutoScrollEnabled(false);
        setShowScrollDownButton(true);
      } else {
        setAutoScrollEnabled(true);
        setShowScrollDownButton(false);
      }
    }
  };

  const handleScrollDown = () => {
    chatContainerRef.current?.scrollTo({
      top: chatContainerRef.current.scrollHeight,
      behavior: 'smooth',
    });
  };

  const handleSettings = () => {
    setShowSettings(!showSettings);
  };

  const onClearAll = () => {
    if (
      confirm(t<string>('Are you sure you want to clear all messages?')) &&
      selectedConversation
    ) {
      handleUpdateConversation(selectedConversation, {
        key: 'messages',
        value: [],
      });
    }
  };

  const scrollDown = () => {
    if (autoScrollEnabled) {
      messagesEndRef.current?.scrollIntoView(true);
    }
  };
  const throttledScrollDown = throttle(scrollDown, 250);

  // useEffect(() => {
  //   console.log('currentMessage', currentMessage);
  //   if (currentMessage) {
  //     handleSend(currentMessage);
  //     homeDispatch({ field: 'currentMessage', value: undefined });
  //   }
  // }, [currentMessage]);

  useEffect(() => {
    throttledScrollDown();
    selectedConversation &&
      setCurrentMessage(
        selectedConversation.messages[selectedConversation.messages.length - 2],
      );
  }, [selectedConversation, throttledScrollDown]);

  useEffect(() => {
    const observer = new IntersectionObserver(
      ([entry]) => {
        setAutoScrollEnabled(entry.isIntersecting);
        if (entry.isIntersecting) {
          textareaRef.current?.focus();
        }
      },
      {
        root: null,
        threshold: 0.5,
      },
    );
    const messagesEndElement = messagesEndRef.current;
    if (messagesEndElement) {
      observer.observe(messagesEndElement);
    }
    return () => {
      if (messagesEndElement) {
        observer.unobserve(messagesEndElement);
      }
    };
  }, [messagesEndRef]);

  return (
    <div className="relative flex-1 overflow-hidden bg-white dark:bg-[#343541]">
      {
        <>
          <div
            className="max-h-full overflow-x-hidden"
            ref={chatContainerRef}
            onScroll={handleScroll}
          >
            {!loggedIn ? (
              <RegisterDialog />
            ) : (
              <>
                {selectedConversation?.messages.map((message, index) => (
                  <MemoizedChatMessage
                    key={index}
                    message={message}
                    messageIndex={index}
                    onEdit={(editedMessage) => {
                      setCurrentMessage(editedMessage);
                      // discard edited message and the ones that come after then resend
                      handleSend(
                        editedMessage,
                        selectedConversation?.messages.length - index,
                      );
                    }}
                  />
                ))}

                {loading && <ChatLoader />}

                <div
                  className="h-[162px] bg-white dark:bg-[#343541]"
                  ref={messagesEndRef}
                />
              </>
            )}
          </div>

          {loggedIn && (
            <ChatInput
              stopConversationRef={stopConversationRef}
              textareaRef={textareaRef}
              onSend={(message, plugin) => {
                setCurrentMessage(message);
                handleSend(message, 0, plugin);
              }}
              onScrollDownClick={handleScrollDown}
              onRegenerate={() => {
                if (currentMessage) {
                  handleSend(currentMessage, 2, null);
                }
              }}
              showScrollDownButton={showScrollDownButton}
            />
          )}
        </>
      }
    </div>
  );
});
Chat.displayName = 'Chat';
