import { Link } from '@chakra-ui/react';
import React, { AnchorHTMLAttributes } from 'react';

export const LinkA: React.FC<{ isExternal?: boolean } & AnchorHTMLAttributes<HTMLAnchorElement>> = (props) => {
  const { href, children, isExternal, ...rest } = props;
  return (
    <Link href={props.href} isExternal={isExternal}>
      <a href={props.href} {...rest}>
        {children}
      </a>
    </Link>
  );
};
